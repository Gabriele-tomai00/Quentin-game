package quentin.game;

import quentin.network.CommunicationProtocol;
import quentin.network.NetworkHandler;

public class OnlineGameStarter extends GameStarter {

    private final NetworkHandler handler;
    private final OnlineGame game;
    private boolean canPlayPie;

    public OnlineGameStarter(NetworkHandler handler, OnlineGame game) {
        super();
        this.handler = handler;
        this.game = game;
        canPlayPie = game.getCurrentPlayer().color() != BoardPoint.BLACK;
    }

    @Override
    public synchronized boolean processInput(String command) {
        return switch (command) {
            case "pie" -> {
                if (canPlayPie && !handler.isWaiting()) {
                    game.applyPieRule();
                    handler.sendCommands(CommunicationProtocol.pie());
                    canPlayPie = false;
                } else {
                    System.err.println("Cannot apply pie rule!");
                }
                yield true;
            }
            case "exit" -> {
                handler.sendCommands(CommunicationProtocol.exit());
                setContinueGame(false);
                yield true;
            }
            default -> super.processInput(command);
        };
    }

    @Override
    public void showHelper() {
        System.out.printf("  %-25s: %-40s%n", "pie", "Applies pie rule");
        super.showHelper();
    }

    @Override
    public void makeMove(String position) {
        if (!handler.isWaiting()) {
            if (canPlayPie) {
                canPlayPie = false;
            }
            super.makeMove(position);
            handler.sendCommands(CommunicationProtocol.move(position));
        } else {
            System.err.println("Waiting for opponents move");
        }
    }

    @Override
    public void hasWon() {
        BoardPoint winner = null;
        if (getGame().hasWon(BoardPoint.BLACK)) {
            winner = BoardPoint.BLACK;
        } else if (getGame().hasWon(BoardPoint.WHITE)) {
            winner = BoardPoint.WHITE;
        }
        if (winner != null) {
            handler.sendCommands(
                    CommunicationProtocol.winner(
                            winner == getGame().getCurrentPlayer().color() ? "LOST" : "WON"));
            setContinueGame(false);
            display();
            String message = winner == getGame().getCurrentPlayer().color() ? "WON" : "LOST";
            System.out.println("YOU " + message + "!!!!!!");
        }
    }

    @Override
    public void processCannotPlay() {
        handler.sendCommands(CommunicationProtocol.change());
    }

    @Override
    public Game getGame() {
        return game;
    }
}
