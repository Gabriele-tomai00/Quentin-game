package quentin;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CacheHandler {
  // the program uploads and downloads in List logCache during the game.
  public final List<BoardLog> logsInMemory;
  // If the program is closed, it uses the cache saved in disk
  private static final String CACHE_DIR = System.getProperty("user.home") + "/.quentinCache";
  private static final String CACHE_FILE = CACHE_DIR + "/quentin_cache.dat";
  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
  private final BufferedWriter writer;
  private int moveIndex; // helps to decide when and what logs to delete

  // LOG STRUCTURE: yyyyMMdd_HHmmss board next_move
  // example: 20241208_172807 1....B............B............B............B............B W

  public CacheHandler() {
    this.logsInMemory = new ArrayList<>();
    this.moveIndex = -1;

    // Crea la directory di cache, se non esiste
    File cacheDirectory = new File(CACHE_DIR);
    if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
      throw new RuntimeException("Failed to create cache directory: " + CACHE_DIR);
    }

    try {
      this.writer = new BufferedWriter(new FileWriter(CACHE_FILE, true));
    } catch (IOException e) {
      throw new RuntimeException("Error initializing file writer", e);
    }
  }

  public int getMoveIndex() {
    return moveIndex;
  }

  public void saveLog(String compactBoard, Player nextPlayer) {
    try {
      if (moveIndex != logsInMemory.size() - 1) {
        removeElementsAfterIndex(moveIndex); // REMOVE ALL ELEMENT AFTER INDEX ELEMENT
      }

      String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
      writer.write(timestamp + " " + compactBoard + " " + nextPlayer.color());
      writer.newLine();
      writer.flush(); // Ensure the log is written immediately
      logsInMemory.add(parseStringLog(timestamp + " " + compactBoard + " " + nextPlayer.color()));

    } catch (IOException e) {
      throw new RuntimeException("Error while saving to cache file", e);
    }
    moveIndex = moveIndex + 1;
    System.out.println("moveIndex " + moveIndex + ": " + compactBoard);
  }

  public void removeElementsAfterIndex(int index) {
    if (index > 0 && index <= logsInMemory.size()) {
      logsInMemory.subList(index + 1, logsInMemory.size()).clear();
      emptyCacheFile();
    }
  }

  public void loadLogsInMemoryFromDisk() {
    // loads all the cache logs on the memory list
    try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        logsInMemory.add(parseStringLog(line));
      }
    } catch (IOException e) {
      throw new IllegalStateException("Problem with reading log file", e);
    }
  }

  public BoardLog readLog(int index) {
    if (index > logsInMemory.size() || index < 0) {
      throw new IndexOutOfBoundsException(
          "Index out of bounds in readLog: index " + index + " not present in log list");
    }
    moveIndex = index;
    return logsInMemory.get(index);
  }

  public int getLogCounter() {
    return logsInMemory.size();
  }

  public BoardLog readLastLog() {
    return readLog(logsInMemory.size() - 1);
  }

  private BoardLog parseStringLog(String logLine) {
    String[] toReturn = logLine.split(" ");
    if (toReturn.length != 3) {
      throw new IllegalStateException("Log format is incorrect");
    }
    return new BoardLog(toReturn[0], toReturn[1], toReturn[2]);
  }

  public void clearCache() {
    emptyCacheFile();
    logsInMemory.clear();
  }

  public void emptyCacheFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
      writer.write("");
      writer.flush();
    } catch (IOException e) {
      throw new RuntimeException("Failed to clear the cache file", e);
    }
  }

  public int calculateMoveCounter() {
    return moveIndex + 1;
  }
}
