package quentin.game;

import java.util.Scanner;
import quentin.MoveParser;

public class SimpleGameStarter implements GameStarter {

    protected LocalGame game;
    private final String CLEAR = "\033[H\033[2J";

    public SimpleGameStarter() {
        game = new LocalGame();
    }

    @Override
    public void start() {
        startDisplay();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (!game.canPlayerPlay()) {
                    game.changeCurrentPlayer();
                    continue;
                }
                display();
                displayMessage(String.format("%s >", game.getCurrentPlayer()));
                if (processInput(scanner.nextLine())) {
                    break;
                }
                if (game.hasWon(game.getCurrentPlayer())) {
                    displayWinner();
                    break;
                }
                game.changeCurrentPlayer();
                if (game.hasWon(game.getCurrentPlayer())) {
                    displayWinner();
                    break;
                }
            }
        }
    }

    public boolean processInput(String nextLine) {
        boolean exitGame = false;
        while (true) {
            try {
                Cell cell = new MoveParser(nextLine).parse();
                game.place(cell);
                game.coverTerritories(cell);
                break;
            } catch (RuntimeException e) {
                displayMessage(e.getMessage());
            }
        }
        return exitGame;
    }

    @Override
    public void displayMessage(String format) {
        System.out.println(format);
    }

    @Override
    public void displayWinner() {
        System.out.print(
                CLEAR + String.format("%s has won", game.getCurrentPlayer()).toUpperCase());
    }

    @Override
    public void display() {
        System.out.println(CLEAR + game.getBoard());
    }

    @Override
    public void startDisplay() {
        System.out.println("   ____  _    _            _   _          _____                      ");
        System.out.println(
                "  / __ \\| |  | |          | | (_)        / ____|                     ");
        System.out.println(" | |  | | |  | | ___ _ __ | |_ _ _ __   | |  __  __ _ _ __ ___   ___ ");
        System.out.println(
                " | |  | | |  | |/ _ \\ '_ \\| __| | '_ \\  | | |_ |/ _` | '_ ` _ \\ / _ \\");
        System.out.println(" | |__| | |__| |  __/ | | | |_| | | | | | |__| | (_| | | | | | |  __/");
        System.out.println(
                "  \\___\\_\\\\____/ \\___|_| |_|\\__|_|_| |_|  \\_____|\\__,_|_| |_| |_|\\___|");
        System.out.println(
                " by Luis Bolaños Mures                                                         "
                        + " \n\n\n");

        System.out.println(game.getBoard());
    }
}
