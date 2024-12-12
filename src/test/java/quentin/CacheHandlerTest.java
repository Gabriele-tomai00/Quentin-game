package quentin;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class CacheHandlerTest {

  @Test
  public void testSaveAndReadLog() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);
    cache.clearCache();
    cache.saveLog("1....B............B............B............B............B", whitePlayer);
    cache.saveLog("2....B............B............B............B............B", whitePlayer);
    cache.saveLog("3....B............B............B............B............B", whitePlayer);

    Exception exceptionForNegativeIndex =
        assertThrows(IndexOutOfBoundsException.class, () -> cache.readLog(-1).board());
    assertEquals(
        "Index out of bounds in readLog: index -1 not present in log list",
        exceptionForNegativeIndex.getMessage());
    assertEquals(
        "1....B............B............B............B............B", cache.readLog(0).board());
    assertEquals(
        "2....B............B............B............B............B", cache.readLog(1).board());
    assertEquals(
        "3....B............B............B............B............B", cache.readLog(2).board());

    assertThrows(IndexOutOfBoundsException.class, () -> cache.readLog(3).board());

    cache.clearCache();
  }

  @Test
  public void testRealBoard() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);
    cache.clearCache();

    Board board = new Board();
    cache.saveLog(board.toCompactString(), whitePlayer);
    assertEquals(
        ".........................................................................................................................................................................",
        cache.readLog(0).board());

    cache.clearCache();
  }

  @Test
  public void testReadLastLog() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);
    cache.clearCache();
    cache.saveLog("1....B............B............B............B............B", whitePlayer);
    cache.saveLog("2....B............B............B............B............B", whitePlayer);
    cache.saveLog("3....B............B............B............B............B", whitePlayer);

    Exception exceptionForNegativeIndex =
        assertThrows(IndexOutOfBoundsException.class, () -> cache.readLog(-1).board());
    assertEquals(
        "Index out of bounds in readLog: index -1 not present in log list",
        exceptionForNegativeIndex.getMessage());

    assertEquals(
        "3....B............B............B............B............B", cache.readLastLog().board());
    assertEquals("W", cache.readLastLog().nextMove());
    cache.clearCache();
    CacheHandler newCache = new CacheHandler();
    assertThrows(IndexOutOfBoundsException.class, () -> newCache.readLastLog().board());
  }

  @Test
  public void testLoadLogFromDisk() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);
    cache.clearCache();
    cache.saveLog("1....B............B............B............B............B", whitePlayer);
    cache.saveLog("2....B............B............B............B............B", whitePlayer);
    cache.saveLog("3....B............B............B............B............B", whitePlayer);

    CacheHandler newCache = new CacheHandler();
    // now we don't have logs in memory, but they are stored in the file
    newCache.loadLogsInMemoryFromDisk();
    assertEquals(3, newCache.getLogCounter());
    assertEquals(
        "1....B............B............B............B............B", newCache.readLog(0).board());
    assertEquals(
        "2....B............B............B............B............B", newCache.readLog(1).board());
    assertEquals(
        "3....B............B............B............B............B", newCache.readLog(2).board());
    assertEquals("W", cache.readLastLog().nextMove());
  }

  @Test
  public void testBackAndMove() {
    CacheHandler cache = new CacheHandler();
    Player whitePlayer = new Player(BoardPoint.WHITE);
    cache.clearCache();
    cache.saveLog("1....B............B............B............B............B", whitePlayer);
    cache.saveLog("2....B............B............B............B............B", whitePlayer);
    cache.saveLog("3....B............B............B............B............B", whitePlayer);
    cache.saveLog("4....B............B............B............B............B", whitePlayer);
    cache.saveLog("5....B............B............B............B............B", whitePlayer);
    cache.saveLog("6....B............B............B............B............B", whitePlayer);
    cache.saveLog("7....B............B............B............B............B", whitePlayer);

    /*
    RESULT in memory
    moveIndex 0: 1....B............B............B............B............B
    moveIndex 1: 2....B............B............B............B............B
    moveIndex 2: 3....B............B............B............B............B
    moveIndex 3: 4....B............B............B............B............B
    moveIndex 4: 5....B............B............B............B............B
    moveIndex 5: 6....B............B............B............B............B
    moveIndex 6: 7....B............B............B............B............B

    7 moves

    RESULT in file
    20241212_153312 1....B............B............B............B............B W
    20241212_153312 2....B............B............B............B............B W
    20241212_153312 3....B............B............B............B............B W
    20241212_153312 4....B............B............B............B............B W
    20241212_153312 5....B............B............B............B............B W
    20241212_153312 6....B............B............B............B............B W
    20241212_153312 7....B............B............B............B............B W
    */

    assertEquals(7, cache.getLogCounter());
    assertEquals(6, cache.getMoveIndex());
    assertEquals(7, cache.calculateMoveCounter());

    assertEquals(
        "4....B............B............B............B............B", cache.readLog(3).board());
    // If I invoke readLog, I change the moveIndex
    assertEquals(3, cache.getMoveIndex());
    // I go back in the game, so there are fewer moves
    assertEquals(4, cache.calculateMoveCounter());

    cache.saveLog("new....B............B............B............B............B", whitePlayer);
    //    // If I invoke saveLog, I invoke removeElementsAfterIndex
    assertEquals(4, cache.getMoveIndex());
    assertEquals(5, cache.getLogCounter());
    // The old moves after that are lost, because we changed move, and so the move made is the last
    // one
    assertEquals(5, cache.calculateMoveCounter());

    /*
    RESULT in memory
      20241212_144554 1....B............B............B............B............B W
      20241212_144554 2....B............B............B............B............B W
      20241212_144554 3....B............B............B............B............B W
      20241212_144554 4....B............B............B............B............B W
      20241212_144554 new....B............B............B............B............B W

      5 moves

    RESULT in file
      20241212_150514 new....B............B............B............B............B W
     */
    cache.clearCache();
  }
}
