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
                System.err.println("Unable to initialize cache: " + e.getMessage());
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
            System.err.println("Unable to save cache: " + e.getMessage());
        }
    }

    public static void clearCache(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            System.err.println("Unable to clear cache: " + e.getMessage());
        }
    }
}
