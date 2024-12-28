package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import quentin.game.Board;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.Game;
import quentin.game.LocalGame;
import quentin.game.Player;

class CacheTest {
  Cache<Game> cache = new Cache<>();
  Game game = new LocalGame();

  @Test
  public void simpleCacheTest() {
    Cache<String> cache = new Cache<>();

    cache.saveLog("First");

    Exception notEnoughLogs = assertThrows(IndexOutOfBoundsException.class, () -> cache.goBack(2));
    assertEquals("Index out of bounds in readLog: index -1 not present in log list", notEnoughLogs.getMessage());

    assertEquals("First", cache.getLog());
    cache.saveLog("Second");
    assertAll("Get all elements", () -> assertEquals("Second", cache.getLog()),
              () -> assertEquals("First", cache.goBack()));
    assertThrows(IndexOutOfBoundsException.class, () -> cache.goBack(2));
  }

  @Test
  public void testReadLastLog() {
    Board board = new Board();
    board.placeStone(BoardPoint.BLACK, 4, 5);
    board.placeStone(BoardPoint.BLACK, 2, 3);
    game.place(new Cell(4, 5));
    game.place(new Cell(2, 3));
    cache.saveLog(game);
//    Exception exceptionForNegativeIndex = assertThrows(IndexOutOfBoundsException.class, () -> cache.readLog(-1)
//                                                                                                   .board());
//    assertEquals("Index out of bounds in readLog: index -1 not present in log list",
//                 exceptionForNegativeIndex.getMessage());
    assertEquals(board, cache.getLog()
                             .getBoard());
    assertEquals(new Player(BoardPoint.BLACK), cache.getLog()
                                                    .getCurrentPlayer());

    game.place(new Cell(1, 1));
    board.placeStone(BoardPoint.BLACK, 1, 1);
    cache.saveLog(game);
    assertEquals(board, cache.getLog()
                             .getBoard());
    game.place(new Cell(2, 1));
    Board board2 = board;
    board2.placeStone(BoardPoint.BLACK, 2, 1);
    cache.saveLog(game);
    assertEquals(board, cache.getLog()
                             .getBoard());
    assertEquals(new Player(BoardPoint.BLACK), cache.getLog()
                           .getCurrentPlayer());
    assertEquals(, cache.readLastLog()
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
  public void testLoadLogFromDisk() {
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
    assertEquals("14W14B27B111", cache.readLog(0)
                                      .board());
    assertEquals("B", cache.readLog(0)
                           .nextMove());

    // cache.forceSaveLog();

    CacheHandler newCache = new CacheHandler();
    // now we don't have logs in memory, but they are stored in the file
    newCache.loadLogsInMemoryFromDisk();
    assertAll("Assert new cache works", () -> assertEquals(2, newCache.getLogCounter()),
              () -> assertEquals("29B27B111", newCache.readLog(0)
                                                      .board()),
              () -> assertEquals("14W14B27B111", newCache.readLog(1)
                                                         .board()),
              () -> assertEquals("B", cache.readLastLog()
                                           .nextMove()));
  }
}
