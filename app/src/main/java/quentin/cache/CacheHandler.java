package quentin.cache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import quentin.game.BoardPoint;
import quentin.game.LocalGame;

public class CacheHandler {

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

    // the program uploads and downloads in List logCache during the game.
    public final LinkedList<BoardLog> logsInMemory;
    // If the program is closed, it uses the cache saved in disk
    private static final String GAME_DIR = System.getProperty("user.home") + "/.quentinGame";
    private static final String CACHE_FILE = GAME_DIR + "/last_match_cache.dat";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private int moveIndex; // helps to decide when and what logs to delete

    // LOG STRUCTURE: yyyyMMdd_HHmmss board next_move
    // example: 20241208_172807
    // 1....B............B............B............B............B W

    public CacheHandler() {
        logsInMemory = new LinkedList<>();
        moveIndex = -1;

        // Create directory, it doesn't exist
        File cacheDirectory = new File(GAME_DIR);
        if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
            throw new RuntimeException("Failed to create cache directory: " + GAME_DIR);
        }
    }

    // Cache and cacheWriter: write to disk and add to cache
    public void saveLog(CachedGame game) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
            if (moveIndex != logsInMemory.size() - 1) {
                removeElementsAfterIndex(moveIndex);
            }
            String nextPlayer = game.getCurrentPlayer().color() == BoardPoint.WHITE ? "B" : "W";
            String boardLog = extracted(game, nextPlayer);
            writer.write(boardLog + System.lineSeparator());
            logsInMemory.push(parseStringLog(boardLog));
            if (logsInMemory.size() > 10) {
                logsInMemory.pop();
                if (moveIndex > 0) {
                    moveIndex--;
                }
            }
        } catch (IOException e) {
            System.err.printf("Error while saving to cache filen due to: %s%n", e.getMessage());
        }
        moveIndex++;
    }

    private String extracted(CachedGame game, String nextPlayer) {
        String boardLog =
                game.getTimestampOfLastMove()
                        + " "
                        + game.getBoard().toCompactString()
                        + " "
                        + nextPlayer
                        + " "
                        + game.getMoveCounter();
        return boardLog;
    }

    // Cache: clear elements if not ok
    public void removeElementsAfterIndex(int index) {
        if (index > 0 && index <= logsInMemory.size()) {
            logsInMemory.subList(index + 1, logsInMemory.size()).clear();
            emptyCacheFile();
        }
    }

    // CacheWriter: load from disk a log
    public void loadLogsInMemoryFromDisk() {
        // loads all the cache logs on the memory list
        try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logsInMemory.push(parseStringLog(line));
            }
        } catch (IOException e) {
            System.err.printf("Problem with reading file due to: %s%n", e.getMessage());
        }
    }

    // Cache: gets a log from memory
    public BoardLog readLog(int index) {
        if (index > logsInMemory.size() || index < 0) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds in readLog: index " + index + " not present in log list");
        }
        moveIndex = index;
        return logsInMemory.get(index);
    }

    public void decrementIndex() {
        moveIndex--;
    }

    // Cache
    public BoardLog readLastLog() {
        return readLog(logsInMemory.size() - 1);
    }

    // LogParser
    private BoardLog parseStringLog(String logLine) {
        String[] toReturn = logLine.split(" ");
        if (toReturn.length != 4) {
            throw new IllegalStateException("Log format is incorrect");
        }
        return new BoardLog(toReturn[0], toReturn[1], toReturn[2], Integer.parseInt(toReturn[3]));
    }

    // Cache
    public void clearCache() {
        emptyCacheFile();
        logsInMemory.clear();
        moveIndex = -1;
    }

    // CacheWriter
    public void emptyCacheFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
            writer.write("");
        } catch (IOException e) {
            System.err.printf("Failed to clear the cache file due to: %s%n", e);
        }
    }

    // Cache
    public int getMoveIndex() {
        return moveIndex;
    }

    // Cache
    public int getLogCounter() {
        return logsInMemory.size();
    }
}
