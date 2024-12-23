package quentin;

import java.util.Scanner;

public class TerminalGame implements GameStarter {

    private final String CLEAR;
    private LocalGame game;
    private Scanner scanner;

    public TerminalGame(Scanner scanner) {
        game = new LocalGame();
        CLEAR = "\033[H\033[2J";
        this.scanner = scanner;
    }

    public void run() {
        initialize();
        final String coordinatePattern = "(?i)^[A-M](?:[0-9]|1[0-2])$";

        while (true) {
            printGamePrompt();
            if (!scanner.hasNextLine()) {
                continue;
            }
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "slg", "startlocalgame":
                    initialize();
                    break;
                case "":
                    break;
                case "help":
                    showHelper();
                    break;
                case "back":
                    undoMove();
                    break;
                case "exit":
                    if (game != null) game.stop();
                    return;
                default:
                    if (command.matches(coordinatePattern)) {
                        makeMove(command);
                    } else {
                        System.out.println("Unknown command: " + command);
                    }
                    break;
            }
        }
    }

    private void undoMove() {
        game.goBackOneMove();
        System.out.println(game.getBoard());
    }

    private void showHelper() {
        System.out.println("Available commands:");
        System.out.println(
                "  startlocalgame or slg   Starts a local game (you can play with yourself in this"
                        + " machine)");
        System.out.println("  exit                    Quits the game and exits the program");
        System.out.println("  help                    Shows this help");
        System.out.println(
                "  <coordinates>           Makes a move. Examples: A1 b2 C5 (wrong examples: 5A,"
                        + " 24)");
        System.out.println("  back                    go back one move");
    }

    private void printGamePrompt() {
        try {
            if (game.isInProgress()) {
                if (game.getCurrentPlayer().color() == BoardPoint.BLACK)
                    System.out.print("BlackPlayer > ");
                else if (game.getCurrentPlayer().color() == BoardPoint.WHITE)
                    System.out.print("WhitePlayer > ");
            } else System.out.print("> ");
        } catch (NullPointerException e) {
            System.out.print("> ");
        }
    }

    private void initialize() {
        game = new LocalGame();
        if (game.isOldMatch()) {
            System.out.println("Old match found, do you want to continue? Y or N");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("n") || answer.equals("no")) {
                game.removeOldMatches();
            } else {
                game.getOldMatch();
                System.out.println(
                        "Game loaded! Your last move was at: "
                                + game.getTimestampOfLastMove()
                                + " number of moves: "
                                + game.getMoveCounter());
            }
            System.out.println(game.getBoard());
            System.out.println("It's your turn: " + game.getCurrentPlayer());
        }
    }

    @Override
    public void displayMessage(String format) {
        System.out.printf("%s please place a stone:", game.getCurrentPlayer());
    }

    private void makeMove(String input) {
        if (!game.isInProgress()) {
            System.out.println("There is not a game in progress");
            return;
        }
        char letter = input.charAt(0);
        int number = Integer.parseInt(input.substring(1));
        Cell cell = new Cell(number, game.letterToIndex(letter));
        try {
            game.place(cell);
        } catch (Exception e) {
            System.err.println("Invalid Coordinates");
            return;
        }
        game.coverTerritories(cell);
        if (game.currentPlayerHasWon()) {
            System.out.println(game.getCurrentPlayer() + " has won!");
            return;
        }
        game.changeCurrentPlayer();
        if (!game.canPlayerPlay()) {

            System.out.print("The next player " + game.getCurrentPlayer() + " can't play");
            game.changeCurrentPlayer();
            System.out.println(", so the next player is " + game.getCurrentPlayer());
            return;
        }
        System.out.println(game.getBoard());
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

    protected Cell getInput(Scanner scanner) {
        return new Parser(scanner.next()).parse();
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub

    }
}
