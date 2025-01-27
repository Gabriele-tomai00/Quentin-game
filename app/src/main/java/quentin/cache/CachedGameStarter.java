package quentin.cache;

import java.time.LocalDateTime;
import java.util.Scanner;
import quentin.game.LocalGame;
import quentin.game.SimpleGameStarter;

public class CachedGameStarter extends SimpleGameStarter {

    private final Cache<GameLog> cache;
    private boolean gameFinished = true;

    public CachedGameStarter() {
        super();
        this.cache = new Cache<>();
        this.cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
    }

    public CachedGameStarter(Cache<GameLog> cache) {
        super();
        if (cache != null && cache.getMemorySize() > 0) {
            game = cache.getLog().game();
            this.cache = cache;
        } else {
            this.cache = new Cache<>();
            this.cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));

            game = new LocalGame();
        }
    }

    @Override
    public boolean processInput(Scanner scanner) {
        boolean exitGame = false;
        while (true) {
            displayMessage("QuentinGame - local mode\n" + game.getCurrentPlayer() + " > ");
            String command = scanner.nextLine().trim().toLowerCase();
            try {
                switch (command) {
                    case "back", "b" -> game = new LocalGame(cache.goBack().game());
                    case "forward", "f" -> game = new LocalGame(cache.goForward().game());
                    case "exit" -> {
                        exitGame = true;
                        gameFinished = false;
                    }
                    case "help" -> showHelper();
                    default -> {
                        makeMove(command);
                        if (hasWon()) {
                            display();
                            exitGame = true;
                        }
                        cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
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

    public void showHelper() {
        System.out.println("Available commands:");
        String[][] commands = {
            {"exit", "Quits the game and exits the program"},
            {"help", "Shows this help"},
            {"<coordinates>", "Makes a move. Examples: A1 b2 C5 (wrong examples: 5A, 24)"},
            {"back or b", "go back one move"},
            {"forward or f", "go forward one move"}
        };
        for (String[] strings : commands) {
            System.out.printf("  %-25s: %-40s%n", strings[0], strings[1]);
        }
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public Cache<GameLog> getCache() {
        return cache;
    }
}
