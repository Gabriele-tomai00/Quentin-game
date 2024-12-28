package quentin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import quentin.cache.BoardLog;
import quentin.cache.Cache;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.GameStarter;
import quentin.game.LocalGame;
import quentin.game.Player;

public class TerminalGame implements GameStarter {

    private final String CLEAR;
    private LocalGame game;
    private Scanner scanner;
    private Cache<BoardLog> cache;
    private boolean gameIsRunning;

    public TerminalGame(Scanner scanner) {
        game = new LocalGame();
        CLEAR = "\033[H\033[2J";
        this.scanner = scanner;
        gameIsRunning = false;
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
                case "slg", "startlocalgame" -> initialize();
                case "" -> {}
                case "help" -> showHelper();
                case "back" -> undoMove();
                // if (game != null) game.stop();
                case "exit" -> {
                    return;
                }
                default -> {
                    if (command.matches(coordinatePattern)) {
                        makeMove(command);
                    } else {
                        System.out.println("Unknown command: " + command);
                    }
                }
            }
        }
    }

    private void undoMove() {
        BoardLog log = cache.goBack();
        game.getBoard().fromCompactString(log.board());
        game.setCurrentPlayer(
                new Player(log.nextMove().equals("W") ? BoardPoint.WHITE : BoardPoint.BLACK));
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
        if (!gameIsRunning) {
            System.out.println("> ");
        } else {
            if (game.getCurrentPlayer().color() == BoardPoint.BLACK) {
                System.out.print("BlackPlayer > ");
            } else {
                System.out.print("WhitePlayer > ");
            }
        }
    }

    private void initialize() {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(CLEAR)))) {
            String line;
            LinkedList<BoardLog> logs = new LinkedList<>();
            while ((line = br.readLine()) != null) {
                String[] strings = line.split(" ");
                logs.add(
                        new BoardLog(
                                strings[0], strings[1], strings[2], Integer.valueOf(strings[3])));
            }
            cache.loadLogs(logs);
            if (cache.getMemorySize() > 0) {
                System.out.println("Old match found, do you want to continue? Y or N");
                String answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("n") || answer.equals("no")) {
                    cache.clear();
                } else {
                    BoardLog log = cache.getLog();
                    System.out.println(
                            "Game loaded! Your last move was at: "
                                    + log.timestamp()
                                    + " number of moves: "
                                    + log.moveCounter());
                    game.getBoard().fromCompactString(log.board());
                    game.setCurrentPlayer(
                            log.nextMove().equals("W")
                                    ? new Player(BoardPoint.WHITE)
                                    : new Player(BoardPoint.BLACK));
                }
                System.out.println(game.getBoard());
                System.out.println("It's your turn: " + game.getCurrentPlayer());
                gameIsRunning = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayMessage(String format) {
        System.out.printf("%s please place a stone:", game.getCurrentPlayer());
    }

    private void makeMove(String input) {
        Cell cell = new MoveParser(input).parse();
        game.place(cell);
        game.coverTerritories(cell);
        if (game.hasWon(game.getCurrentPlayer())) {
            displayWinner();
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

    @Override
    public void start() {
        // TODO Auto-generated method stub

    }
}
