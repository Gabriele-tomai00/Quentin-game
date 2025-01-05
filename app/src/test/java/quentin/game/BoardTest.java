package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class BoardTest {

    Board board = new Board();

    @Test
    public void testCorrectValuesInMatrix() {
        board.placeStone(BoardPoint.BLACK, 2, 2);
        board.placeStone(BoardPoint.WHITE, 2, 3);
        assertDoesNotThrow(() -> board);
    }

    @Test
    public void testCorrectPlaceStone() {
        board.placeStone(BoardPoint.BLACK, 1, 2);
        board.placeStone(BoardPoint.WHITE, 3, 4);
        assertAll(
                () -> assertEquals(BoardPoint.BLACK, board.getPoint(new Cell(1, 2))),
                () -> assertEquals(BoardPoint.WHITE, board.getPoint(new Cell(3, 4))));
    }

    @Test
    public void testToCompactStringAndFromCompactString() {
        for (int i = 0; i < board.size(); i++) {
            board.placeStone(BoardPoint.BLACK, i, 4);
        }
        String expectedResult = board.toCompactString();
        assertEquals(expectedResult, board.toCompactString());

        Board newBoard = new Board();
        newBoard.fromCompactString(expectedResult);
        assertEquals(expectedResult, newBoard.toCompactString());

        board = new Board();
        expectedResult = "169";
        assertEquals(expectedResult, board.toCompactString());
        board = new Board();
        board.placeStone(BoardPoint.BLACK, 2, 3);
        board.placeStone(BoardPoint.BLACK, 4, 5);
        expectedResult = "29B27B111";
        assertEquals(expectedResult, board.toCompactString());
        newBoard = new Board();
        newBoard.fromCompactString(expectedResult);
        assertEquals(expectedResult, newBoard.toCompactString());
        assertEquals(newBoard, board);
    }

    @Test
    public void gameCopyIsEqual() {
        board.placeStone(BoardPoint.BLACK, 0, 0);
        Board board2 = new Board();
        board2.setBoard(board);
        assertEquals(board, board2);
    }
}
