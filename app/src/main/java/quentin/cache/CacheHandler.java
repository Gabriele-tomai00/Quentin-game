package quentin.cache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
import quentin.game.Board;

public class CacheHandler {
    private static final String GAME_DIR = System.getProperty("user.home") + "/.quentinGame";
    private static final String CACHE_FILE = GAME_DIR + "/last_match_cache.dat";

    @SuppressWarnings("unchecked")
    public static Cache<GameLog> initialize() {
        Cache<GameLog> cache = new Cache<>();
        File file = new File(CACHE_FILE);

        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                cache = (Cache<GameLog>) input.readObject();
                System.out.println("Cache loaded successfully from " + CACHE_FILE);
            } catch (IOException | ClassNotFoundException e) {
                // e.getMessage());
            }
        } else {
            System.out.println("Cache file does not exist. Using a new cache.");
        }
        return cache;
    }

    public static void saveCache(Cache<GameLog> cache) {
        if (cache.getMemorySize() == 1
                && Objects.equals(cache.getLog().game().getBoard(), new Board())) return;
        File cacheDirectory = new File(GAME_DIR);
        if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
            throw new RuntimeException("Failed to create cache directory: " + GAME_DIR);
        }
        clearCache();
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(CACHE_FILE))) {
            output.writeObject(cache);
        } catch (IOException e) {
            System.out.println("Failed to save cache: " + CACHE_FILE);
        }
    }

    public static void clearCache() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear the cache file");
        }
    }
}
