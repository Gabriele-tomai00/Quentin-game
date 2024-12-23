package quentin;

// JUnit 5
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test; // Test of JUnit 5

import quentin.exceptions.MoveException;

public class CacheHandlerTest {
  LocalGame game = new LocalGame();

  @Test
  public void testSaveAndReadLog() throws MoveException {
    CacheHandler cache = new CacheHandler();
    cache.clearCache();

    game.place(new Cell(4, 5));
    game.place(new Cell(2, 3));
    cache.saveLog(game);

    Exception exceptionForNegativeIndex = assertThrows(IndexOutOfBoundsException.class, () -> cache.readLog(-1)
                                                                                                   .board());
    assertEquals("Index out of bounds in readLog: index -1 not present in log list",
                 exceptionForNegativeIndex.getMessage());

    assertEquals("29B27B111", cache.readLog(0)
                                   .board());
    assertEquals("W", cache.readLog(0)
                           .nextMove());

    game.changeCurrentPlayer();
    game.place(new Cell(1, 1));
    cache.saveLog(game);
    assertEquals("14W14B27B111", cache.readLog(1)
                                      .board());
    assertEquals("B", cache.readLog(1)
                           .nextMove());

    assertThrows(IndexOutOfBoundsException.class, () -> cache.readLog(2)
                                                             .board());

    cache.clearCache();
  }

  @Test
  public void testReadLastLog() throws MoveException {
    CacheHandler cache = new CacheHandler();
    cache.clearCache();
    game.place(new Cell(4, 5));
    game.place(new Cell(2, 3));
    cache.saveLog(game);
    Exception exceptionForNegativeIndex = assertThrows(IndexOutOfBoundsException.class, () -> cache.readLog(-1)
                                                                                                   .board());
    assertEquals("Index out of bounds in readLog: index -1 not present in log list",
                 exceptionForNegativeIndex.getMessage());
    assertEquals("29B27B111", cache.readLog(0)
                                   .board());
    assertEquals("W", cache.readLastLog()
                           .nextMove());

    game.changeCurrentPlayer();
    game.place(new Cell(1, 1));
    cache.saveLog(game);
    assertEquals("14W14B27B111", cache.readLog(1)
                                      .board());

    game.changeCurrentPlayer();
    game.place(new Cell(2, 1));
    cache.saveLog(game);
    assertEquals("14W12B1B27B111", cache.readLog(2)
                                        .board());
    assertEquals("W", cache.readLog(2)
                           .nextMove());

    assertEquals("14W12B1B27B111", cache.readLastLog()
                                        .board());
    assertEquals("W", cache.readLastLog()
                           .nextMove());
    cache.clearCache();
    CacheHandler newCache = new CacheHandler();
    assertThrows(IndexOutOfBoundsException.class, () -> newCache.readLastLog()
                                                                .board());

    cache.clearCache();
  }

  @Test
  public void testLoadLogFromDisk() throws MoveException {
    CacheHandler cache = new CacheHandler();
    cache.clearCache();

    game.place(new Cell(4, 5));
    game.place(new Cell(2, 3));
    cache.saveLog(game);
    assertEquals("29B27B111", cache.readLog(0)
                                   .board());
    assertEquals("W", cache.readLog(0)
                           .nextMove());

    game.changeCurrentPlayer();
    game.place(new Cell(1, 1));
    cache.saveLog(game);
    assertEquals("14W14B27B111", cache.readLog(1)
                                      .board());
    assertEquals("B", cache.readLog(1)
                           .nextMove());

    cache.forceSaveLog();

    CacheHandler newCache = new CacheHandler();
    // now we don't have logs in memory, but they are stored in the file
    newCache.loadLogsInMemoryFromDisk();
    assertEquals(2, newCache.getLogCounter());
    assertEquals("29B27B111", newCache.readLog(0)
                                      .board());
    assertEquals("14W14B27B111", newCache.readLog(1)
                                         .board());
    assertEquals("B", cache.readLastLog()
                           .nextMove());
  }
}
