/* (C)2024 */
package quentin;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class BoardPointTest {

    @Test
    public void testFromString() {
        assertEquals(BoardPoint.BLACK, BoardPoint.fromString("B"));
        assertEquals(BoardPoint.BLACK, BoardPoint.fromString("Black"));
        assertEquals(BoardPoint.WHITE, BoardPoint.fromString("W"));
        assertEquals(BoardPoint.WHITE, BoardPoint.fromString("White"));
        assertEquals(BoardPoint.EMPTY, BoardPoint.fromString("."));
        assertEquals(BoardPoint.EMPTY, BoardPoint.fromString("Unknown")); // Default case
    }

    @Test
    public void testToString() {
        assertEquals("B", BoardPoint.BLACK.toString());
        assertEquals("W", BoardPoint.WHITE.toString());
        assertEquals(".", BoardPoint.EMPTY.toString());
    }

    @Test
    public void testToMatrixString() {
        BoardPoint[][] board = {
            {BoardPoint.BLACK, BoardPoint.WHITE, BoardPoint.EMPTY},
            {BoardPoint.EMPTY, BoardPoint.BLACK, BoardPoint.WHITE}
        };

        String[][] expectedMatrix = {
            {"B", "W", "."},
            {".", "B", "W"}
        };

        assertArrayEquals(expectedMatrix, BoardPoint.toMatrixString(board));
    }

    @Test
    public void testFromMatrixString() {
        String[][] inputMatrix = {
            {"B", "W", "."},
            {".", "B", "W"}
        };

        BoardPoint[][] expectedBoard = {
            {BoardPoint.BLACK, BoardPoint.WHITE, BoardPoint.EMPTY},
            {BoardPoint.EMPTY, BoardPoint.BLACK, BoardPoint.WHITE}
        };

        assertArrayEquals(expectedBoard, BoardPoint.fromMatrixString(inputMatrix));
    }

    @Test
    public void testRoundTripConversion() {
        BoardPoint[][] board = {
            {BoardPoint.BLACK, BoardPoint.WHITE, BoardPoint.EMPTY},
            {BoardPoint.EMPTY, BoardPoint.BLACK, BoardPoint.WHITE}
        };

        String[][] stringMatrix = BoardPoint.toMatrixString(board);
        BoardPoint[][] convertedBoard = BoardPoint.fromMatrixString(stringMatrix);

        assertArrayEquals(board, convertedBoard);
    }
}
