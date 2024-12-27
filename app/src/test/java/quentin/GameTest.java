package quentin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import quentin.cache.CachedGame;
import quentin.exceptions.CellAlreadyTakenException;
import quentin.exceptions.IllegalMoveException;
import quentin.exceptions.MoveException;
import quentin.game.Board;
import quentin.game.BoardPoint;
import quentin.game.Cell;

public class GameTest {
    CachedGame game = new CachedGame();
    LocalGame localGame = new LocalGame();

    @Test
    public void initializationTest() {
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        assertEquals(BoardPoint.BLACK, localGame.getCurrentPlayer().color());
    }

    @Test
    public void testMoveIsValid() {
        assertTrue(localGame.isMoveValid(localGame.getCurrentPlayer().color(), new Cell(0, 0)));
        localGame.place(new Cell(0, 0));
        assertTrue(localGame.isMoveValid(localGame.getCurrentPlayer().color(), new Cell(12, 12)));
        // Now it is not valid
        assertFalse(localGame.isMoveValid(localGame.getCurrentPlayer().color(), new Cell(1, 1)));
        localGame.place(new Cell(0, 1));
        // Now it is
        assertTrue(localGame.isMoveValid(localGame.getCurrentPlayer().color(), new Cell(1, 1)));
        localGame.changeCurrentPlayer();
        localGame.place(new Cell(1, 1));
        localGame.changeCurrentPlayer();
        assertTrue(localGame.isMoveValid(localGame.getCurrentPlayer().color(), new Cell(2, 1)));
        localGame.place(new Cell(2, 1));
    }

    @Test
    void exceptionsTest() {
        localGame.place(new Cell(0, 0));
        MoveException exception =
                assertThrows(
                        CellAlreadyTakenException.class,
                        () ->
                                localGame.isMoveValid(
                                        localGame.getCurrentPlayer().color(), new Cell(0, 0)));
        String expectedMessage = "Cell (a, 1) is not empty!";
        String actualMessage = exception.getMessage();
        assertTrue(expectedMessage.contains(actualMessage));
        MoveException exception2 =
                assertThrows(IllegalMoveException.class, () -> localGame.place(new Cell(1, 1)));
        expectedMessage = "Cell (b, 2), is not connected to other cells of the same color!";
        actualMessage = exception2.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void canPlayerPlay() {
        assertEquals(true, localGame.canPlayerPlay());
    }

    @Test
    public void testChangePlayer() {
        assertEquals(BoardPoint.BLACK, localGame.getCurrentPlayer().color());
        localGame.changeCurrentPlayer();
        assertEquals(BoardPoint.WHITE, localGame.getCurrentPlayer().color());
    }

    @Test
    public void playerPlays() {
        Board board = new Board();
        board.placeStone(BoardPoint.BLACK, 0, 0);

        localGame.place(new Cell(0, 0));

        assertEquals(board, localGame.getBoard());
        assertThrows(MoveException.class, () -> localGame.place(new Cell(0, 0)));
    }

    @Test
    public void neighborsTest() {
        Set<Cell> neigbors = new HashSet<Cell>();
        neigbors.add(new Cell(0, 0));
        neigbors.add(new Cell(1, 1));
        neigbors.add(new Cell(2, 0));
        assertEquals(neigbors, localGame.getNeighbors(new Cell(1, 0)));
    }

    @Test
    public void findNoTerritory() {
        Cell cell = new Cell(0, 0);

        localGame.place(cell);

        assertEquals(Collections.<Cell>emptySet(), localGame.findTerritories(cell));
        assertEquals(Collections.emptySet(), localGame.findTerritories(new Cell(0, 1)));
    }

    @Test
    public void findOneCellTerritory() {

        localGame.place(new Cell(0, 0));
        localGame.place(new Cell(0, 1));
        localGame.place(new Cell(1, 1));
        localGame.place(new Cell(2, 1));
        localGame.place(new Cell(3, 1));
        localGame.place(new Cell(3, 0));

        Set<Cell> testSet = new HashSet<Cell>();
        testSet.add(new Cell(1, 0));
        testSet.add(new Cell(2, 0));
        assertEquals(testSet, localGame.findTerritories(new Cell(1, 0)));
    }

    @Test
    public void findLargeTerritory() {
        for (int i = 0; i < localGame.boardSize(); i++) {

            localGame.place(new Cell(i, 10));
            localGame.changeCurrentPlayer();
            localGame.place(new Cell(i, 8));
            localGame.changeCurrentPlayer();
        }
        HashSet<Cell> testSet = new HashSet<Cell>();
        for (int i = 0; i < localGame.boardSize(); i++) {
            testSet.add(new Cell(i, 9));
        }
        assertEquals(BoardPoint.BLACK, localGame.getBoard().getPoint(new Cell(0, 10)));
        assertEquals(BoardPoint.WHITE, localGame.getBoard().getPoint(new Cell(0, 8)));
        assertEquals(BoardPoint.EMPTY, localGame.getBoard().getPoint(new Cell(0, 9)));
        assertEquals(BoardPoint.BLACK, localGame.getBoard().getPoint(new Cell(10, 10)));
        assertEquals(testSet, localGame.findTerritories(new Cell(9, 9)));
    }

    @Test
    public void territoriesAreCovered() {
        try {
            localGame.place(new Cell(0, 11));
            localGame.place(new Cell(1, 11));
            localGame.place(new Cell(1, 12));
            localGame.coverTerritories(new Cell(1, 12));

            // small 2 cells black territory near top left corner
            localGame.place(new Cell(0, 0));
            for (int i = 0; i < 4; i++) {
                localGame.place(new Cell(i, 1));
            }
            localGame.place(new Cell(3, 0));
            localGame.coverTerritories(new Cell(3, 0));

            // small 1 cell black territory
            localGame.place(new Cell(1, 2));
            localGame.place(new Cell(2, 2));
            localGame.place(new Cell(3, 2));
            localGame.place(new Cell(3, 3));
            localGame.changeCurrentPlayer();
            localGame.place(new Cell(1, 3));
            localGame.place(new Cell(1, 4));
            localGame.place(new Cell(2, 4));
            localGame.coverTerritories(new Cell(2, 4));
            for (int i = 0; i < localGame.boardSize(); i++) {
                localGame.changeCurrentPlayer();
                localGame.place(new Cell(i, 8));
                localGame.changeCurrentPlayer();
                localGame.place(new Cell(i, 10));
            }
            localGame.coverTerritories(new Cell(12, 10));
        } catch (MoveException e) {
            // do nothing
        }
        assertAll(
                "Territories are being covered",
                () ->
                        assertEquals(
                                BoardPoint.BLACK, localGame.getBoard().getPoint(new Cell(0, 12))),
                () -> assertEquals(BoardPoint.BLACK, localGame.getBoard().getPoint(new Cell(1, 0))),
                () -> assertEquals(BoardPoint.BLACK, localGame.getBoard().getPoint(new Cell(2, 0))),
                () -> assertEquals(BoardPoint.EMPTY, localGame.getBoard().getPoint(new Cell(0, 3))),
                () -> assertEquals(BoardPoint.BLACK, localGame.getBoard().getPoint(new Cell(2, 3))),
                () ->
                        assertEquals(
                                BoardPoint.BLACK, localGame.getBoard().getPoint(new Cell(0, 9))));
    }

    @Test
    public void letterToIndexTest() {
        assertEquals(0, game.letterToIndex('A'));
        assertEquals(1, game.letterToIndex('B'));
        assertEquals(2, game.letterToIndex('C'));
        assertEquals(3, game.letterToIndex('D'));
        assertEquals(4, game.letterToIndex('E'));
        assertEquals(5, game.letterToIndex('F'));
        assertEquals(6, game.letterToIndex('G'));
        assertEquals(7, game.letterToIndex('H'));
        assertEquals(8, game.letterToIndex('I'));
        assertEquals(9, game.letterToIndex('J'));
        assertEquals(10, game.letterToIndex('K'));
        assertEquals(11, game.letterToIndex('L'));
        assertEquals(12, game.letterToIndex('M'));

        assertEquals(-1, game.letterToIndex('O'));
        assertEquals(-1, game.letterToIndex('Z'));
    }
}
