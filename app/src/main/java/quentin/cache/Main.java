package quentin.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import quentin.game.LocalGame;

public class Main {

    private static final String GAME_DIR = System.getProperty("user.home") + "/.quentinGame";
    private static final String CACHE_FILE = GAME_DIR + "/last_match_cache.dat";

    public static void main(String[] args) {
        CachedGameStarter gameStarter = initialize();
        gameStarter.start();
        if (!gameStarter.isGameFinished()) {
            saveCache(gameStarter.getCache());
        }
    }

    @SuppressWarnings("unchecked")
    public static CachedGameStarter initialize() {
        if (new File(GAME_DIR).exists()) {
            try (ObjectInputStream input =
                    new ObjectInputStream(new FileInputStream(new File(CACHE_FILE)))) {
                return new CachedGameStarter((Cache<LocalGame>) input.readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new CachedGameStarter();
    }

    public static void saveCache(Cache<LocalGame> cache) {
        try (ObjectOutputStream output =
                new ObjectOutputStream(new FileOutputStream(new File(CACHE_FILE)))) {
            output.writeObject(cache + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
