package quentin.game;

import java.util.Scanner;

public abstract class GameStarter implements Runnable {
    protected static final String CLEAR = "\033[H\033[2J";
    private Scanner scanner;
    private boolean continueGame;

    protected GameStarter() {
        scanner = new Scanner(System.in);
        continueGame = true;
    }

    public abstract Game getGame();

    public abstract void processCannotPlay();

    @Override
    public void run() {
        display();
        while (continueGame) {
            if (hasWon() != null) {
                displayWinner();
                break;
            }
            if (!getGame().canPlayerPlay()) {
                processCannotPlay();
            } else {
                do {
                    displayMessage(String.format("%s > %n", getGame().getCurrentPlayer()));
                } while (!processInput(scanner.nextLine()));
                display();
                processCannotPlay();
            }
        }
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
        getGame().place(cell);
        getGame().coverTerritories(cell);
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

    public BoardPoint hasWon() {
        if (getGame().hasWon(BoardPoint.WHITE)) {
            return BoardPoint.WHITE;
        }
        if (getGame().hasWon(BoardPoint.BLACK)) {
            return BoardPoint.BLACK;
        }
        return null;
    }

    public void setContinueGame(boolean wantToContinue) {
        continueGame = wantToContinue;
    }

    public void displayMessage(String format) {
        System.out.print(format);
    }

    public void displayWinner() {
        System.out.println(CLEAR + String.format("%s has won", new Player(hasWon())).toUpperCase());
    }

    public void display() {
        System.out.println(CLEAR + getGame().getBoard());
    }
}
