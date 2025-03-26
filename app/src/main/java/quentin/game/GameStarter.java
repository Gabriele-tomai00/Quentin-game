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
        while (continueGame) {
            display();
            if (!getGame().canPlayerPlay()) {
                processCannotPlay();
            } else {
                do {
                    System.out.printf("%s > %n", getGame().getCurrentPlayer());
                } while (!processInput(scanner.nextLine()));
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
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void makeMove(String position) {
        Cell cell = new MoveParser(position).parse();
        getGame().place(cell);
        getGame().coverTerritories(cell);
        hasWon();
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

    public void hasWon() {
        BoardPoint winner = null;
        if (getGame().hasWon(BoardPoint.WHITE)) {
            winner = BoardPoint.WHITE;
        }
        if (getGame().hasWon(BoardPoint.BLACK)) {
            winner = BoardPoint.BLACK;
        }
        if (winner != null) {
            System.out.println(
                    CLEAR + String.format("%s has won", new Player(winner)).toUpperCase());
            continueGame = false;
        }
    }

    public void setContinueGame(boolean wantToContinue) {
        continueGame = wantToContinue;
    }

    public void display() {
        System.out.println(CLEAR + getGame().getBoard());
    }
}
