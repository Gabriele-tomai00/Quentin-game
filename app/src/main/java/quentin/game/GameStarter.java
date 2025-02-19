package quentin.game;

import java.util.Scanner;

public class GameStarter implements Runnable {
    protected String CLEAR = "\033[H\033[2J";
    protected LocalGame game;
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
            if (game.hasWon(game.getCurrentPlayer())) {
                displayWinner();
                break;
            }
            if (!game.canPlayerPlay()) {
                game.changeCurrentPlayer();
            } else {
                while (!processInput(scanner.nextLine())) {}
                display();
                if (game.hasWon(game.getCurrentPlayer())) {
                    displayWinner();
                    break;
                }
                game.changeCurrentPlayer();
            }
        }
    }

    public boolean processInput(String command) {
        displayMessage(String.format("%s > ", game.getCurrentPlayer()));
        try {
            switch (command) {
                case "exit" -> {
                    continueGame = false;
                    return true;
                }
                case "help" -> showHelper();
                default -> {
                    makeMove(command);
                    return true;
                }
            }
        } catch (RuntimeException e) {
            displayMessage(e.getMessage());
        }
        return false;
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
