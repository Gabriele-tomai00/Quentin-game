package quentin.cache;

import java.time.LocalDateTime;
import quentin.game.GameStarter;
import quentin.game.LocalGame;

public class CachedGameStarter extends GameStarter {

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

    //  @Override
    //  public void start() {
    //    super.start();
    //    if (gameFinished) { CacheHandler.saveCache(cache); }
    //  }

    @Override
    public boolean processInput(String command) {
        displayMessage(String.format("%s > ", game.getCurrentPlayer()));
        switch (command) {
            case "back", "b" -> game = new LocalGame(cache.goBack().game());
            case "forward", "f" -> game = new LocalGame(cache.goForward().game());
            case "exit" -> {
                setContinueGame(false);
                gameFinished = false;
                return true;
            }
            default -> {
                return super.processInput(command);
            }
        }
        return true;
    }

    @Override
    public void makeMove(String position) {
        super.makeMove(position);
        cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
    }

    @Override
    public void showHelper() {
        super.showHelper();
        String[][] commands = {
            {"back or b", "go back one move"}, {"forward or f", "go forward one move"}
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
