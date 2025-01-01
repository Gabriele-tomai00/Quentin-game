package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import quentin.game.Cell;
import quentin.game.LocalGame;

public class CacheHandlerTest {
    CacheHandler cache = new CacheHandler();
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Test
    public void readAndWriteCacheToFile() {
        Cache<GameLog> cache = new Cache<>();
        LocalGame game = new LocalGame();
        game.place(new Cell(0, 0));
        game.changeCurrentPlayer();
        cache.saveLog(
                new GameLog(LocalDateTime.now().format(TIMESTAMP_FORMATTER), new LocalGame(game)));
        game.place(new Cell(1, 0));
        game.changeCurrentPlayer();
        cache.saveLog(
                new GameLog(LocalDateTime.now().format(TIMESTAMP_FORMATTER), new LocalGame(game)));
        CacheHandler.saveCache(cache);
        Cache<GameLog> cache2 = CacheHandler.initialize();
        assertEquals(cache2, cache);
    }

    /*
     * @Test public void testSaveAndReadLog() { cache.clearCache();
     *
     * game.place(new Cell(4, 5)); game.place(new Cell(2, 3)); cache.saveLog(game);
     *
     * Exception exceptionForNegativeIndex =
     * assertThrows(IndexOutOfBoundsException.class, () ->
     * cache.readLog(-1).board()); assertEquals(
     * "Index out of bounds in readLog: index -1 not present in log list",
     * exceptionForNegativeIndex.getMessage());
     *
     * assertEquals("29B27B111", cache.readLog(0).board()); assertEquals("W",
     * cache.readLog(0).nextMove());
     *
     * game.changeCurrentPlayer(); game.place(new Cell(1, 1)); cache.saveLog(game);
     * assertEquals("14W14B27B111", cache.readLog(0).board()); assertEquals("B",
     * cache.readLog(0).nextMove());
     *
     * assertThrows(IndexOutOfBoundsException.class, () ->
     * cache.readLog(2).board());
     *
     * cache.clearCache(); }
     *
     * @Test public void testReadLastLog() { cache.clearCache(); game.place(new
     * Cell(4, 5)); game.place(new Cell(2, 3)); cache.saveLog(game); Exception
     * exceptionForNegativeIndex = assertThrows(IndexOutOfBoundsException.class, ()
     * -> cache.readLog(-1).board()); assertEquals(
     * "Index out of bounds in readLog: index -1 not present in log list",
     * exceptionForNegativeIndex.getMessage()); assertEquals("29B27B111",
     * cache.readLog(0).board()); assertEquals("W", cache.readLastLog().nextMove());
     *
     * game.changeCurrentPlayer(); game.place(new Cell(1, 1)); cache.saveLog(game);
     * assertEquals("14W14B27B111", cache.readLog(0).board());
     *
     * game.changeCurrentPlayer(); game.place(new Cell(2, 1)); cache.saveLog(game);
     * assertEquals("14W12B1B27B111", cache.readLog(0).board()); assertEquals("W",
     * cache.readLog(0).nextMove());
     *
     * assertEquals("29B27B111", cache.readLastLog().board()); assertEquals("W",
     * cache.readLastLog().nextMove()); cache.clearCache(); CacheHandler newCache =
     * new CacheHandler(); assertThrows(IndexOutOfBoundsException.class, () ->
     * newCache.readLastLog().board());
     *
     * cache.clearCache(); }
     *
     * @Test public void testLoadLogFromDisk() { cache.clearCache();
     *
     * game.place(new Cell(4, 5)); game.place(new Cell(2, 3)); cache.saveLog(game);
     * assertEquals("29B27B111", cache.readLog(0).board()); assertEquals("W",
     * cache.readLog(0).nextMove());
     *
     * game.changeCurrentPlayer(); game.place(new Cell(1, 1)); cache.saveLog(game);
     * assertEquals("14W14B27B111", cache.readLog(0).board()); assertEquals("B",
     * cache.readLog(0).nextMove());
     *
     * // cache.forceSaveLog();
     *
     * CacheHandler newCache = new CacheHandler(); // now we don't have logs in
     * memory, but they are stored in the file newCache.loadLogsInMemoryFromDisk();
     * assertAll( "Assert new cache works", () -> assertEquals(2,
     * newCache.getLogCounter()), () -> assertEquals("29B27B111",
     * newCache.readLog(0).board()), () -> assertEquals("14W14B27B111",
     * newCache.readLog(1).board()), () -> assertEquals("B",
     * cache.readLastLog().nextMove())); }
     */
}
