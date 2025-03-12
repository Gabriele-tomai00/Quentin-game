package quentin.network;

import java.util.Scanner;
import quentin.game.BoardPoint;
import quentin.game.GameStarter;

public class OnlineGameStarter extends GameStarter {

    private final NetworkHandler handler;
    private final OnlineGame game;
    private boolean canPlayPie;
    private boolean continueGame = true;
    private final Scanner scanner = new Scanner(System.in);

    public OnlineGameStarter(NetworkHandler handler, OnlineGame game) {
        this.handler = handler;
        this.game = game;
        canPlayPie = game.getCurrentPlayer().color() != BoardPoint.BLACK;
    }

    @Override
    public void run() {
        display();
        while (continueGame) {
            if (hasWon()) {
                displayWinner();
                break;
            }
            if (!game.canPlayerPlay()) {
                processCannotPlay();
            } else {
                do {
                    displayMessage(String.format("%s > %n", game.getCurrentPlayer()));
                } while (!processInput(scanner.nextLine()));
                display();
                if (hasWon()) {
                    displayWinner();
                    break;
                }
            }
        }
    }

    @Override
    public synchronized boolean processInput(String command) {
        return switch (command) {
            case "pie" -> {
                if (canPlayPie) {
                    game.applyPieRule();
                    handler.sendCommands(command);
                    canPlayPie = false;
                } else {
                    System.err.println("Cannot apply pie rule!");
                }
                yield true;
            }
            case "exit" -> {
                handler.sendCommands(command);
                setContinueGame(false);
                yield true;
            }
            default -> super.processInput(command);
        };
    }

    @Override
    public void makeMove(String position) {
        if (!handler.isWaiting()) {
            if (canPlayPie) {
                canPlayPie = false;
            }
            super.makeMove(position);
            handler.sendCommands(position);
        } else {
            System.err.println("Waiting for opponents move");
        }
    }

    @Override
    public void processCannotPlay() {
        handler.sendCommands("change");
    }
}
