package quentin.game;

import java.util.Scanner;

public class GameStarter implements Runnable {
    protected static final String CLEAR = "\033[H\033[2J";
    private LocalGame game;
    private Scanner scanner;
    private boolean continueGame = true;

    public GameStarter() {
        game = new LocalGame();
        scanner = new Scanner(System.in);
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
                processCannotPlay();
            }
        }
    }

    public void processCannotPlay() {
      game.changeCurrentPlayer();
    }

    public boolean processInput(String command) {
        try {
            return switch (command) {
                case "" -> false;
                case "exit" -> {
                    continueGame = false;
                    yield true;
                }
                case "help" -> {
                    showHelper();
                    yield false;
                }
                default -> {
                    makeMove(command);
                    yield true;
                }
            };
        } catch (RuntimeException e) {
            displayError(e.getMessage());
        }
        return false;
    }

    private void displayError(String message) {
        System.err.println(message);
    }

    public void makeMove(String position) {
        Cell cell = new MoveParser(position).parse();
        game.place(cell);
        game.coverTerritories(cell);
    }

    public void showHelper() {
        System.out.println("Available commands:");
        String[][] commands = {
            {"exit", "Quits the game and exits the program"},
            {"help", "Shows this help"},
            {"<coordinates>", "Makes a move. Examples: A1 b2 C5 (wrong examples: 5A, 24)"}
        };
        for (String[] strings : commands) {
            System.out.printf("  %-25s: %-40s%n", strings[0], strings[1]);
        }
    }

    public boolean hasWon() {
        return game.hasWon(game.getCurrentPlayer());
    }

    public Game getGame() {
        return game;
    }

    public void setContinueGame(boolean wantToContinue) {
        continueGame = wantToContinue;
    }

    public void displayMessage(String format) {
        System.out.print(format);
    }

    public void displayWinner() {
        System.out.println(
                CLEAR + String.format("%s has won", getGame().getCurrentPlayer()).toUpperCase());
    }

    public void display() {
        System.out.println(CLEAR + getGame().getBoard());
    }
}
