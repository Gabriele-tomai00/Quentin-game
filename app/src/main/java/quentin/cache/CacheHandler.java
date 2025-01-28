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
import quentin.exceptions.CacheClearException;
import quentin.exceptions.CacheSaveException;
import quentin.game.GameBoard;

public class CacheHandler {
    private static final String GAME_DIR = ".quentinGame";
    private static final String CACHE_FILE = GAME_DIR + "/last_match_cache.dat";

    private CacheHandler() {
        throw new UnsupportedOperationException(
                "CacheHandler is a utility class and cannot be instantiated.");
    }

    @SuppressWarnings("unchecked")
    public static Cache<GameLog> initialize() {
        Cache<GameLog> cache = new Cache<>();
        File file = new File(CACHE_FILE);

        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                cache = (Cache<GameLog>) input.readObject();
                System.out.println("Cache loaded successfully from " + CACHE_FILE);
            } catch (IOException e) {
                System.err.println("Failed to load cache due to an IO error: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println(
                        "Failed to load cache: Class not found. This may indicate incompatible"
                                + " serialization.");
            }
        } else {
            System.out.println("Cache file does not exist. Using a new cache.");
        }

        return cache;
    }

    public static void saveCache(Cache<GameLog> cache) {
        if (cache.getMemorySize() == 1
                && Objects.equals(cache.getLog().game().getBoard(), new GameBoard())) {
            return;
        }
        File cacheDirectory = new File(GAME_DIR);
        if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
            throw new CacheSaveException("Failed to create cache directory: " + GAME_DIR);
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
            throw new CacheClearException("Failed to clear the cache file: " + CACHE_FILE, e);
        }
    }
}
