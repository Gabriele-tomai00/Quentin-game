package quentin.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import quentin.game.LocalGame;

public class CacheHandler {
    private static final String GAME_DIR = System.getProperty("user.home") + "/.quentinGame";
    private static final String CACHE_FILE = GAME_DIR + "/last_match_cache.dat";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @SuppressWarnings("unchecked")
    public static CachedGameStarter initialize() {
        CachedGameStarter starter = null;
        if (new File(GAME_DIR).exists()) {
            try (ObjectInputStream input =
                    new ObjectInputStream(new FileInputStream(new File(CACHE_FILE)))) {
                starter = new CachedGameStarter((Cache<LocalGame>) input.readObject());
                if (starter != null) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        System.out.println("Old match found, do you want to continue? Y or N");
                        String answer = scanner.nextLine().trim().toLowerCase();
                        if (answer.equals("y") || answer.equals("yes")) {
                            return starter;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new CachedGameStarter();
    }

    public static void saveCache(Cache<LocalGame> cache) {
        File cacheDirectory = new File(GAME_DIR);
        if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
            throw new RuntimeException("Failed to create cache directory: " + GAME_DIR);
        }
        try (ObjectOutputStream output =
                new ObjectOutputStream(new FileOutputStream(new File(CACHE_FILE)))) {
            output.writeObject(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
