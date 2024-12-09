package quentin;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class CacheHandlerTest {

  @Test
  public void testWriteLog() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);

    cache.writeLog("1....B............B............B............B............B", whitePlayer);
    assertEquals(
        "1....B............B............B............B............B", cache.readLog(0).board());
    cache.clearCache(); // for the next test
  }

  @Test
  public void testGetLogCounter() {

    // calculateMoveCounter() read all the logs in the file and return the result (should use only
    // once inside the class)
    // getLogCounter() return the number of logs/moves that is updated every time a log is written
    // on the file (to use outside the class)
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);
    cache.clearCache();

    cache.writeLog("1....B............B............B............B............B", whitePlayer);
    cache.writeLog("2....B............B............B............B............B", whitePlayer);
    cache.writeLog("3....B............B............B............B............B", whitePlayer);
    assertEquals(3, cache.getLogCounter());
    cache.clearCache();

    cache.writeLog("1....B............B............B............B............B", whitePlayer);
    cache.writeLog("2....B............B............B............B............B", whitePlayer);
    cache.clearCache();

    cache.writeLog("1....B............B............B............B............B", whitePlayer);
    cache.writeLog("2....B............B............B............B............B", whitePlayer);
    cache.writeLog("1....B............B............B............B............B", whitePlayer);
    cache.writeLog("2....B............B............B............B............B", whitePlayer);
    assertEquals(4, cache.getLogCounter());
    cache.closeWriter();

    CacheHandler cache1 =
        new CacheHandler(); // I open again the same cache file and I get the same counter
    assertEquals(4, cache1.getLogCounter());

    cache.clearCache(); // for the next test
  }

  @Test
  public void testWriteLogs() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);
    Player blackPlayer = new Player(BoardPoint.BLACK);
    cache.clearCache();

    cache.writeLog("1....B............B............B............B............B", whitePlayer);
    cache.writeLog("2....B............B............B............B............B", blackPlayer);
    cache.writeLog("3....B............B............B............B............B", whitePlayer);

    assertEquals(
        "1....B............B............B............B............B", cache.readLog(0).board());
    assertEquals(
        "2....B............B............B............B............B", cache.readLog(1).board());
    assertEquals(
        "3....B............B............B............B............B", cache.readLog(2).board());

    assertEquals("W", cache.readLog(0).nextMove());
    assertEquals("B", cache.readLog(1).nextMove());
    assertEquals("W", cache.readLog(2).nextMove());
    cache.clearCache(); // for the next test
  }

  @Test
  public void testReadLastLog() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);
    cache.clearCache();

    cache.writeLog("1....B............B............B............B............B", whitePlayer);
    cache.writeLog("2....B............B............B............B............B", whitePlayer);
    cache.writeLog("3....B............B............B............B............B", whitePlayer);

    assertEquals(
        "3....B............B............B............B............B", cache.readLastLog().board());
    cache.clearCache(); // for the next test
  }

  @Test
  public void testMainSimulation() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);

    cache.writeLog("1....B............B............B............B............B", whitePlayer);
    cache.writeLog("2....B............B............B............B............B", whitePlayer);
    cache.writeLog("3....B............B............B............B............B", whitePlayer);

    assertEquals(
        "3....B............B............B............B............B", cache.readLastLog().board());

    // close the program
    CacheHandler cache1 = new CacheHandler();
    Player blackPlayer = new Player(BoardPoint.BLACK);
    cache.writeLog("4....B............B............B............B............B", blackPlayer);
    assertEquals(
        "4....B............B............B............B............B", cache.readLastLog().board());
    assertEquals(
        "4....B............B............B............B............B", cache.readLog(3).board());

    cache1.clearCache(); // for the next test
  }
}
