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

    //    @Test
    //    void testEmptyBoardCompactString() {
    //        String expectedResult = "169";
    //        assertEquals(expectedResult, board.toCompactString());
    //    }
    //
    //    @Test
    //    void testCompactString2Tiles() {
    //        board.placeStone(BoardPoint.BLACK, 2, 3);
    //        board.placeStone(BoardPoint.BLACK, 4, 5);
    //        String expectedResult = "29B27B111";
    //        Board newBoard = new Board();
    //        newBoard.fromCompactString(expectedResult);
    //        assertAll(
    //                () -> assertEquals(expectedResult, board.toCompactString()),
    //                () -> assertEquals(expectedResult, board.toCompactString()),
    //                () -> assertEquals(newBoard, board));
    //    }
    //
    //    @Test
    //    void testToAndFromCompactString() {
    //        for (int i = 0; i < board.size(); i++) {
    //            board.placeStone(BoardPoint.BLACK, i, 4);
    //        }
    //        String expectedResult = board.toCompactString();
    //        assertEquals("4B12B12B12B12B12B12B12B12B12B12B12B12B8", expectedResult);
    //
    //        board = new Board();
    //        board.fromCompactString(expectedResult);
    //        assertEquals("4B12B12B12B12B12B12B12B12B12B12B12B12B8", expectedResult);
    //    }

    @Test
    void gameCopyIsEqual() {
        board.placeStone(BoardPoint.BLACK, 0, 0);
        Board board2 = new Board();
        board2.setBoard(board);
        assertEquals(board, board2);
    }
}
