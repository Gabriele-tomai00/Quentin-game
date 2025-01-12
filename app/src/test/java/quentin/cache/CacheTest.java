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
    Cache<LocalGame> cache = new Cache<>();
    LocalGame game = new LocalGame();

    @Test
    public void simpleCacheTest() {
        Cache<String> cache = new Cache<>();

        cache.saveLog("First");

        Exception notEnoughLogs =
                assertThrows(IndexOutOfBoundsException.class, () -> cache.goBack(2));
        assertEquals("you can't go back one more move", notEnoughLogs.getMessage());

        assertEquals("First", cache.getLog());
        cache.saveLog("Second");
        assertAll(
                () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals("Second", cache.getLog()),
                () -> assertEquals("First", cache.goBack()));
        cache.saveLog("Third");
        assertAll(
                () -> assertEquals("Third", cache.getLog()),
                        () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals("First", cache.goBack()),
                        () -> assertEquals("Third", cache.goForward()));
        assertThrows(IndexOutOfBoundsException.class, () -> cache.goBack(2));
    }

    @Test
    public void goBackOnce() {
        Board board1 = new Board();
        board1.placeStone(BoardPoint.BLACK, 4, 5);
        Board board2 = new Board();
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
    public void goBackAndForwardTest() {
        game.place(new Cell(4, 5));
        cache.saveLog(new LocalGame(game));
        game.place(new Cell(2, 3));
        cache.saveLog(new LocalGame(game));
        Board board1 = new Board();
        board1.placeStone(BoardPoint.BLACK, 4, 5);
        Board board2 = new Board();
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
    public void addAfterGoingBackTest() {
        game.place(new Cell(4, 5));
        cache.saveLog(new LocalGame(game));
        game.place(new Cell(2, 3));
        cache.saveLog(new LocalGame(game));
        game = new LocalGame(cache.goBack());
        game.place(new Cell(1, 1));
        cache.saveLog(new LocalGame(game));
        Board board1 = new Board();
        board1.placeStone(BoardPoint.BLACK, 4, 5);
        Board board2 = new Board();
        board2.setBoard(board1);
        board2.placeStone(BoardPoint.BLACK, 1, 1);
        assertAll(
                () -> assertEquals(2, cache.getMemorySize()),
                () -> assertEquals(board2, cache.getLog().getBoard()),
                () -> assertEquals(board1, cache.goBack().getBoard()));
    }
}
