package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import quentin.game.Board;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.LocalGame;
import quentin.game.Player;

class CacheTest {

    @Test
    public void simpleCacheTest() {
        Cache<String> cache = new Cache<>();

        cache.saveLog("First");

        Exception notEnoughLogs =
                assertThrows(IndexOutOfBoundsException.class, () -> cache.goBack(2));
        assertEquals("Index: 2, Size: 1", notEnoughLogs.getMessage());

        assertEquals("First", cache.getLog());
        cache.saveLog("Second");
        assertAll(
                () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals("Second", cache.getLog()),
                () -> assertEquals("First", cache.goBack()),
                () -> cache.saveLog("Third"),
                () -> assertEquals("Third", cache.getLog()),
                () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals("First", cache.goBack()),
                () -> assertEquals("Third", cache.goForward()));
        assertThrows(IndexOutOfBoundsException.class, () -> cache.goBack(2));
    }

    @Test
    public void testReadLastLog() {
        Cache<LocalGame> cache = new Cache<>();
        LocalGame game = new LocalGame();
        Board board = new Board();
        board.placeStone(BoardPoint.BLACK, 4, 5);
        board.placeStone(BoardPoint.BLACK, 2, 3);
        game.place(new Cell(4, 5));
        game.place(new Cell(2, 3));
        cache.saveLog(new LocalGame(game));
        assertEquals(1, cache.getMemorySize());
        assertEquals(board, cache.getLog().getBoard());
        assertEquals(new Player(BoardPoint.BLACK), cache.getLog().getCurrentPlayer());
        game.place(new Cell(1, 1));
        Board board2 = new Board();
        board2.setBoard(board);
        board2.placeStone(BoardPoint.BLACK, 1, 1);
        cache.saveLog(new LocalGame(game));
        assertAll(
                () -> assertEquals(board2, cache.getLog().getBoard()),
                () -> assertEquals(board, cache.goBack().getBoard()));
        game = new LocalGame(cache.getLog());
        game.place(new Cell(2, 1));
        Board board3 = new Board();
        board3.setBoard(board);
        board3.placeStone(BoardPoint.BLACK, 2, 1);
        cache.saveLog(new LocalGame(game));
        assertAll(
                () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals(board3, cache.getLog().getBoard()),
                () -> assertEquals(new Player(BoardPoint.BLACK), cache.getLog().getCurrentPlayer()),
                () -> assertEquals(board, cache.goBack().getBoard()),
                () ->
                        assertEquals(
                                new Player(BoardPoint.BLACK), cache.getLog().getCurrentPlayer()));
    }

    /*
     * @Test public void testLoadLogFromDisk() { game.place(new Cell(4, 5));
     * game.place(new Cell(2, 3)); cache.saveLog(game); assertEquals("29B27B111",
     * cache.readLog(0) .board()); assertEquals("W", cache.readLog(0) .nextMove());
     *
     * game.changeCurrentPlayer(); game.place(new Cell(1, 1)); cache.saveLog(game);
     * assertEquals("14W14B27B111", cache.readLog(0) .board()); assertEquals("B",
     * cache.readLog(0) .nextMove());
     *
     * // cache.forceSaveLog();
     *
     * CacheHandler newCache = new CacheHandler(); // now we don't have logs in
     * memory, but they are stored in the file newCache.loadLogsInMemoryFromDisk();
     * assertAll("Assert new cache works", () -> assertEquals(2,
     * newCache.getLogCounter()), () -> assertEquals("29B27B111",
     * newCache.readLog(0) .board()), () -> assertEquals("14W14B27B111",
     * newCache.readLog(1) .board()), () -> assertEquals("B", cache.readLastLog()
     * .nextMove())); }
     */
}
