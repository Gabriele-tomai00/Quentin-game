package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test; // Test of JUnit 5

class BoardTest {

    Board board = new Board();

    @Test
    void testCorrectValuesInMatrix() {
        assertDoesNotThrow(() -> board.placeStone(BoardPoint.BLACK, 2, 2));
        assertDoesNotThrow(() -> board.placeStone(BoardPoint.WHITE, 2, 3));
    }

    @Test
    void testCorrectPlaceStone() {
        board.placeStone(BoardPoint.BLACK, 1, 2);
        board.placeStone(BoardPoint.WHITE, 3, 4);
        assertAll(
                () -> assertEquals(BoardPoint.BLACK, board.getPoint(new Cell(1, 2))),
                () -> assertEquals(BoardPoint.WHITE, board.getPoint(new Cell(3, 4))));
    }

    @Test
    void boardCopyIsEqual() {
        board.placeStone(BoardPoint.BLACK, 0, 0);
        Board board2 = new Board();
        board2.setBoard(board);
        assertEquals(board, board2);
    }
}
