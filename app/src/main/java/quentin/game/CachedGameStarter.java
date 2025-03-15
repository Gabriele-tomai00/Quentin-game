package quentin.game;

import java.time.LocalDateTime;
import quentin.cache.Cache;
import quentin.cache.GameLog;

public class CachedGameStarter extends GameStarter {

    private final Cache<GameLog> cache;
    private boolean gameFinished = true;
    private LocalGame game;

    public CachedGameStarter() {
        super();
        this.cache = new Cache<>();
        this.game = new LocalGame();
    }

    public CachedGameStarter(Cache<GameLog> cache) {
        super();
        if (cache != null && cache.getMemorySize() > 0) {
            game = new LocalGame(cache.getLog().game());
            this.cache = cache;
        } else {
            this.cache = new Cache<>();
            game = new LocalGame();
        }
    }

    @Override
    public boolean processInput(String command) {
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

    @Override
    public void processCannotPlay() {
        game.changeCurrentPlayer();
    }

    @Override
    public Game getGame() {
        return game;
    }
}
