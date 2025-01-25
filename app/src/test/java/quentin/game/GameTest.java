package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import quentin.exceptions.CellAlreadyTakenException;
import quentin.exceptions.IllegalMoveException;
import quentin.exceptions.MoveException;

class GameTest {
    LocalGame game = new LocalGame();

    @Test
    void initializationTest() {
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
    }

    @Test
    void testMoveIsValid() {
        assertTrue(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(0, 0)));
        game.place(new Cell(0, 0));
        assertTrue(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(12, 12)));
        assertFalse(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(1, 1)));
        game.place(new Cell(0, 1));
        assertTrue(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(1, 1)));
        game.changeCurrentPlayer();
        game.place(new Cell(1, 1));
        game.changeCurrentPlayer();
        assertTrue(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(2, 1)));
        game.place(new Cell(2, 1));
    }

    @Test
    void testMoveA1IsValid() {
        assertTrue(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(0, 0)));
    }

    @Test
    void testMoveM13IsValid() {
        assertTrue(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(12, 12)));
    }

    @Test
    void testB2IsNotValid() {
        game.place(new Cell(0, 0));
        assertFalse(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(1, 1)));
    }

    @Test
    void testB2NowIsValid() {
        game.place(new Cell(0, 0));
        game.place(new Cell(0, 1));
        assertTrue(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(1, 1)));
    }

    @Test
    void testAlsoWorksWithWhite() {
        game.changeCurrentPlayer();
        game.place(new Cell(1, 1));
        game.changeCurrentPlayer();
        assertTrue(game.isMoveValid(game.getCurrentPlayer().color(), new Cell(2, 1)));
    }

    @Test
    void exceptionsTest() {
        game.place(new Cell(0, 0));
        Cell cell = new Cell(0, 0);
        BoardPoint color = game.getCurrentPlayer().color();
        MoveException exception =
                assertThrows(CellAlreadyTakenException.class, () -> game.isMoveValid(color, cell));
        String expectedMessage = "Cell (a, 1) is not empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        Cell cell2 = new Cell(1, 1);
        MoveException exception2 =
                assertThrows(IllegalMoveException.class, () -> game.place(cell2));
        expectedMessage = "Cell (b, 2), is not connected to other cells of the same color!";
        actualMessage = exception2.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void canPlayerPlay() {
        assertEquals(true, game.canPlayerPlay());
    }

    @Test
    void testChangePlayer() {
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        game.changeCurrentPlayer();
        assertEquals(BoardPoint.WHITE, game.getCurrentPlayer().color());
    }

    @Test
    void playerPlays() {
        GameBoard board = new GameBoard();
        board.placeStone(BoardPoint.BLACK, 0, 0);
        game.place(new Cell(0, 0));
        assertEquals(board, game.getBoard());
        Cell cell = new Cell(0, 0);
        assertThrows(MoveException.class, () -> game.place(cell));
    }

    @Test
    void neighborsTest() {
        Set<Cell> neigbors = new HashSet<Cell>();
        neigbors.add(new Cell(0, 0));
        neigbors.add(new Cell(1, 1));
        neigbors.add(new Cell(2, 0));
        assertEquals(neigbors, game.getNeighbors(new Cell(1, 0)));
    }

    @Test
    void findNoTerritory() {
        Cell cell = new Cell(0, 0);

        game.place(cell);

        assertEquals(Collections.<Cell>emptySet(), game.findTerritories(cell));
        assertEquals(Collections.emptySet(), game.findTerritories(new Cell(0, 1)));
    }

    @Test
    void findOneCellTerritory() {

        game.place(new Cell(0, 0));
        game.place(new Cell(0, 1));
        game.place(new Cell(1, 1));
        game.place(new Cell(2, 1));
        game.place(new Cell(3, 1));
        game.place(new Cell(3, 0));

        Set<Cell> testSet = new HashSet<Cell>();
        testSet.add(new Cell(1, 0));
        testSet.add(new Cell(2, 0));
        assertEquals(testSet, game.findTerritories(new Cell(1, 0)));
    }

    @Test
    void findLargeTerritory() {
        for (int i = 0; i < game.boardSize(); i++) {

            game.place(new Cell(i, 10));
            game.changeCurrentPlayer();
            game.place(new Cell(i, 8));
            game.changeCurrentPlayer();
        }
        HashSet<Cell> testSet = new HashSet<Cell>();
        for (int i = 0; i < game.boardSize(); i++) {
            testSet.add(new Cell(i, 9));
        }
        assertAll(
                () -> assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(0, 10))),
                () -> assertEquals(BoardPoint.WHITE, game.getBoard().getPoint(new Cell(0, 8))),
                () -> assertEquals(BoardPoint.EMPTY, game.getBoard().getPoint(new Cell(0, 9))),
                () -> assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(10, 10))),
                () -> assertEquals(testSet, game.findTerritories(new Cell(9, 9))));
    }

    @Test
    void smallTerritoryIsCovered() {
        game.place(new Cell(0, 11));
        game.place(new Cell(1, 11));
        game.place(new Cell(1, 12));
        game.coverTerritories(new Cell(1, 12));
        assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(0, 12)));
    }

    @Test
    void small2CellsTerritoryIsCoveredTest() {
        game.place(new Cell(0, 0));
        for (int i = 0; i < 4; i++) {
            game.place(new Cell(i, 1));
        }
        game.place(new Cell(3, 0));
        game.coverTerritories(new Cell(3, 0));
        assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(1, 0)));
        assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(2, 0)));
    }

    @Test
    void oneCellTerritoryBothPlayersTest() {
        game.place(new Cell(1, 2));
        game.place(new Cell(2, 2));
        game.place(new Cell(3, 2));
        game.place(new Cell(3, 3));
        game.changeCurrentPlayer();
        game.place(new Cell(1, 3));
        game.place(new Cell(1, 4));
        game.place(new Cell(2, 4));
        game.coverTerritories(new Cell(2, 4));
        assertEquals(BoardPoint.EMPTY, game.getBoard().getPoint(new Cell(0, 3)));
        assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(2, 3)));
    }

    @Test
    void longLineBothPlayersTest() {
        for (int i = 0; i < game.boardSize(); i++) {
            game.changeCurrentPlayer();
            game.place(new Cell(i, 8));
            game.changeCurrentPlayer();
            game.place(new Cell(i, 10));
        }
        game.coverTerritories(new Cell(12, 10));
        assertEquals(BoardPoint.WHITE, game.getBoard().getPoint(new Cell(0, 9)));
    }

    @Test
    void constructorCopyTest() {
        game.changeCurrentPlayer();
        game.place(new Cell(0, 0));
        LocalGame localGame2 = new LocalGame(game);
        assertEquals(localGame2.getCurrentPlayer(), game.getCurrentPlayer());
        assertEquals(game.getBoard(), localGame2.getBoard());
    }
}
