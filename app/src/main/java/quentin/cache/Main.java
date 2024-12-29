package quentin.cache;

public class Main {

    public static void main(String[] args) {
        CachedGameStarter gameStarter = CacheHandler.initialize();
        gameStarter.start();
        if (!gameStarter.isGameFinished()) {
            CacheHandler.saveCache(gameStarter.getCache());
        }
    }
}
