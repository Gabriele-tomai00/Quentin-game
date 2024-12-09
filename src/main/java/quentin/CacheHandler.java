package quentin;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CacheHandler {
  private static final String CACHE_DIR = System.getProperty("user.home") + "/.quentinCache";
  private static final String CACHE_FILE = CACHE_DIR + "/quentin_cache.dat";
  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
  private final BufferedWriter writer;
  private int
      logCounter; // It's always updated (you can get a specific log without reading all the cache
  // file every time)

  // LOG STRUCTURE: yyyyMMdd_HHmmss board next_move
  // example: 20241208_172807 1....B............B............B............B............B W

  public CacheHandler() {
    File cacheDirectory = new File(CACHE_DIR);
    if (!cacheDirectory.exists()) {
      if (!cacheDirectory.mkdirs()) {
        throw new RuntimeException("Failed to create cache directory: " + CACHE_DIR);
      }
    }
    logCounter = calculateMoveCounter(); // used only one
    try {
      // Open the writer only once in the constructor
      this.writer = new BufferedWriter(new FileWriter(CACHE_FILE, true));
    } catch (IOException e) {
      throw new RuntimeException("Error initializing file writer", e);
    }
  }

  public void writeLog(String compactBoard, Player nextPlayer) {
    try {
      String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
      writer.write(timestamp + " " + compactBoard + " " + nextPlayer.color().toString());
      writer.newLine();
      writer.flush(); // Ensure the log is written immediately
      logCounter++; // I update the log counter, so I can know the number of moves without reading
      // all the file every time
    } catch (IOException e) {
      throw new RuntimeException("Error while saving to cache file", e);
    }
  }

  // Metodo per leggere il log dato un indice
  public BoardLog readLog(int index) {

    if (index < 0 || index >= logCounter) {
      throw new IllegalStateException("Invalid log index");
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE))) {
      String line;
      int counter = 0;
      while ((line = reader.readLine()) != null) {
        if (counter == index) {
          return parseLogLine(line);
        }
        counter++;
      }
    } catch (IOException e) {
      throw new IllegalStateException("Problem with reading log file", e);
    }
    return null; // Se non trovato, ritorna null
  }

  // Funzione per caricare l'ultimo log
  public BoardLog readLastLog() {
    if (logCounter == 0) {
      return null; // No logs
    }
    try {
      return readLog(logCounter - 1);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to read last log", e);
    }
  }

  private BoardLog parseLogLine(String logLine) {
    String[] toReturn = logLine.split(" ");
    if (toReturn.length != 3) {
      throw new IllegalStateException("Log format is incorrect");
    }
    return new BoardLog(toReturn[0], toReturn[1], toReturn[2]);
  }

  public void clearCache() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
      // Svuota il contenuto del file, senza eliminarlo
      writer.write(""); // Scrive una stringa vuota, svuotando il file
      writer.flush();
      logCounter = 0;
    } catch (IOException e) {
      throw new RuntimeException("Failed to clear the cache file", e);
    }
  }

  public void closeWriter() {
    try {
      if (writer != null) writer.close();
    } catch (IOException e) {
      throw new RuntimeException("Error while closing writer", e);
    }
  }

  private int calculateMoveCounter() {
    try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE))) {
      int counter = 0;
      while (reader.readLine() != null) {
        counter++;
      }
      return counter;
    } catch (IOException e) {
      throw new IllegalStateException(
          "Unable to read the log file. The number of moves cannot be determined.");
    }
  }

  public int getLogCounter() {
    return logCounter;
  }
}
