package quentin.network;

import java.util.Scanner;
import quentin.game.GameStarter;

public class OnlineGameStarter extends GameStarter {

    private NetworkHandler handler;
    private OnlineGame game;

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            display();
            while (true) {
                if (game.hasWon(game.getCurrentPlayer())) {
                    displayWinner();
                    break;
                }
                if (!game.canPlayerPlay()) {
                    handler.sendCommands("change");
                } else {
                    do {
                        displayMessage(String.format("%s > %n", game.getCurrentPlayer()));
                    } while (!processInput(scanner.nextLine()));
                    display();
                    if (game.hasWon(game.getCurrentPlayer())) {
                        displayWinner();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public synchronized boolean processInput(String command) {
        return super.processInput(command);
    }

    @Override
    public void makeMove(String position) {
        super.makeMove(position);
        handler.sendCommands(position);
    }
}
