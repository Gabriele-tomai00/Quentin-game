package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.GameBoard;
import quentin.game.LocalGame;
import quentin.game.Player;

class CacheTest {
    Cache<LocalGame> cache = new Cache<>();
    LocalGame game = new LocalGame();

    @Test
    void simpleCacheTest() {
        Cache<String> stringCache = new Cache<>();

        stringCache.saveLog("First");

        Exception notEnoughLogs =
                assertThrows(IndexOutOfBoundsException.class, () -> stringCache.goBack(2));
        assertEquals("you can't go back one more move", notEnoughLogs.getMessage());

        assertEquals("First", stringCache.getLog());
        stringCache.saveLog("Second");
        assertAll(
                () -> assertEquals(2, stringCache.getMemorySize()),
                () -> assertEquals("Second", stringCache.getLog()),
                () -> assertEquals("First", stringCache.goBack()));
        stringCache.saveLog("Third");
        assertAll(
                () -> assertEquals("Third", stringCache.getLog()),
                () -> assertEquals(2, stringCache.getMemorySize()),
                () -> assertEquals("First", stringCache.goBack()),
                () -> assertEquals("Third", stringCache.goForward()));
        assertThrows(IndexOutOfBoundsException.class, () -> stringCache.goBack(2));
    }

    @Test
    void goBackOnce() {
        GameBoard board1 = new GameBoard();
        board1.placeStone(BoardPoint.BLACK, 4, 5);
        GameBoard board2 = new GameBoard();
        board2.setBoard(board1);
        board2.placeStone(BoardPoint.BLACK, 2, 3);
        game.place(new Cell(4, 5));
        cache.saveLog(new LocalGame(game));
        game.place(new Cell(2, 3));
        cache.saveLog(new LocalGame(game));
        assertAll(
                () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals(board2, cache.getLog().getBoard()),
                () -> assertEquals(new Player(BoardPoint.BLACK), cache.getLog().getCurrentPlayer()),
                () -> assertEquals(board1, cache.goBack().getBoard()));
    }

    @Test
    void goBackAndForwardTest() {
        game.place(new Cell(4, 5));
        cache.saveLog(new LocalGame(game));
        game.place(new Cell(2, 3));
        cache.saveLog(new LocalGame(game));
        GameBoard board1 = new GameBoard();
        board1.placeStone(BoardPoint.BLACK, 4, 5);
        GameBoard board2 = new GameBoard();
        board2.setBoard(board1);
        board2.placeStone(BoardPoint.BLACK, 2, 3);
        assertAll(
                () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals(board2, cache.getLog().getBoard()),
                () -> assertEquals(new Player(BoardPoint.BLACK), cache.getLog().getCurrentPlayer()),
                () -> assertEquals(board1, cache.goBack().getBoard()),
                () -> assertEquals(board2, cache.goForward().getBoard()));
    }

    @Test
    void addAfterGoingBackTest() {
        game.place(new Cell(4, 5));
        cache.saveLog(new LocalGame(game));
        game.place(new Cell(2, 3));
        cache.saveLog(new LocalGame(game));
        game = new LocalGame(cache.goBack());
        game.place(new Cell(1, 1));
        cache.saveLog(new LocalGame(game));
        GameBoard board1 = new GameBoard();
        board1.placeStone(BoardPoint.BLACK, 4, 5);
        GameBoard board2 = new GameBoard();
        board2.setBoard(board1);
        board2.placeStone(BoardPoint.BLACK, 1, 1);
        assertAll(
                () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals(board2, cache.getLog().getBoard()),
                () -> assertEquals(board1, cache.goBack().getBoard()));
    }
}
