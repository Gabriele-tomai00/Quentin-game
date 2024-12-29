package quentin.cache;

import java.util.Scanner;
import quentin.game.LocalGame;
import quentin.game.SimpleGameStarter;

public class CachedGameStarter extends SimpleGameStarter {

    private final Cache<LocalGame> cache;
    private boolean gameFinished = true;

    public CachedGameStarter() {
        super();
        this.cache = new Cache<>();
    }

    public CachedGameStarter(Cache<LocalGame> cache) {
        super();
        game = cache.getLog();
        this.cache = cache;
    }

    @Override
    public boolean processInput(Scanner scanner) {
        boolean exitGame = false;
        while (true) {
            displayMessage(game.getCurrentPlayer() + " >");
            String command = scanner.next();
            try {
                switch (command) {
                    case "back" -> {
                        game = cache.goBack();
                    }
                    case "forward" -> {
                        game = cache.goForward();
                    }
                    case "exit" -> {
                        exitGame = true;
                        gameFinished = false;
                    }
                    case "help" -> showHelper();
                    default -> {
                        makeMove(command);
                        exitGame = hasWon();
                        cache.saveLog(new LocalGame(game));
                    }
                }
            } catch (RuntimeException e) {
                displayMessage(e.getMessage() + "\n");
                continue;
            }
            break;
        }
        return exitGame;
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

    public boolean isGameFinished() {
        return gameFinished;
    }

    public Cache<LocalGame> getCache() {
        return cache;
    }
}
