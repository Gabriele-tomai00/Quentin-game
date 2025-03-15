package quentin.cache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheHandler {
    private static final String CACHE_FILE = ".last_match_cache";

    private CacheHandler() {
        throw new UnsupportedOperationException(
                "CacheHandler is a utility class and cannot be instantiated.");
    }

    @SuppressWarnings("unchecked")
    public static Cache<GameLog> initialize(File file) {
        Cache<GameLog> cache = new Cache<>();
        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                cache = (Cache<GameLog>) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return cache;
    }

    public static void saveCache(File file, Cache<GameLog> cache) {
        if (cache.getMemorySize() <= 1) {
            return;
        }
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearCache() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
