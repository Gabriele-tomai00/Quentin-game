/* (C)2024 */
package quentin;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class BoardTest {

    public static boolean areMatricesEqual(String[][] matrix1, String[][] matrix2) {
        if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
            return false;
        }
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[i].length; j++) {
                if (!matrix1[i][j].equals(matrix2[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void testValidMatrixSize() {
        String[][] array = {
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."}
        };

        assertDoesNotThrow(
                () -> {
                    new Board(array);
                });
    }

    @Test
    public void testInvalidMatrixSize() {
        String[][] invalidBoard = {
            {".", ".", ".", ".", "."},
            {".", "W", ".", ".", "."},
            {".", ".", "B", ".", "."},
            {".", ".", ".", ".", "."},
            {".", ".", ".", ".", "."},
        };

        Exception exception =
                assertThrows(IllegalArgumentException.class, () -> new Board(invalidBoard));
        assertEquals("Matrix size must be: 13x13. Your matrix is 5x5", exception.getMessage());
    }

    @Test
    public void testCorrectValuesInMatrix() {
        // the only allowed values are "." "B" "W"
        String[][] array = {
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", "B", "B", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", "W", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", "W", "W", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."}
        };

        assertDoesNotThrow(
                () -> {
                    new Board(array);
                });
    }

    @Test
    public void testIncorrectValuesInMatrix() {
        // the only allowed values are "." "B" "W"
        String[][] array = {
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", "B", "B", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", "A", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", "W", "C", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", "W", "W", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", null, ".", ".", ".", ".", ".", ".", "M", ".", ".", ".", "."}
        };

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Board(array));
        assertEquals(
                "Not valid value in matrix: (A in 8,3 position). The only allowed values are '.',"
                        + " 'B' and 'W'",
                exception.getMessage());
    }

    @Test
    public void testSomeBoardFunctionality() {
        Board board = new Board();
        String result = board.toString();
        String expectedOutput =
                """
                          0     1    2    3   4     5    6    7    8    9    10  11  12
                             B     B    B    B   B     B    B    B    B    B    B   B
                     W  ┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐ W
                0       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                1       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                2       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                3       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                4       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                5       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                6       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                7       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                8       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                9       │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                10      │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                11      │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W
                12      │    │    │    │    │    │    │    │    │    │    │    │    │    │
                     W  └────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘ W
                          B     B    B    B   B     B    B    B    B    B    B   B     B
                """;
        assertEquals(expectedOutput, result);
    }

    /*
     * @Test public void testisMoveFistMoveValid() { Board board = new Board();
     * Player p = new Player(BoardPoint.BLACK); // valid
     * assertTrue(board.isMoveValid(p, 0, 0, true)); assertTrue(board.isMoveValid(p,
     * 12, 12, true)); assertTrue(board.isMoveValid(p, 6, 6, true));
     *
     * // not valid assertFalse(board.isMoveValid(p, -1, 0, true));
     * assertFalse(board.isMoveValid(p, 0, -1, true));
     * assertFalse(board.isMoveValid(p, 13, 0, true));
     * assertFalse(board.isMoveValid(p, 0, 13, true));
     * assertFalse(board.isMoveValid(p, 14, 14, true)); }
     *
     * @Test public void testisMoveValid() { Board board = new Board(); Player p =
     * new Player(BoardPoint.BLACK); // the board is empty, so it's not possibile to
     * put a stone because there are // no other stones, and It's not the first move
     * assertFalse(board.isMoveValid(p, 0, 0, false));
     * assertFalse(board.isMoveValid(p, 12, 12, false));
     * assertFalse(board.isMoveValid(p, 6, 6, false)); // now I add a stone with the
     * same color (so the same player) and I try to put // another stone
     * orthogonally close // CORNER 1 board.placeStone(BoardPoint.BLACK, 0, 0);
     * assertTrue(board.isMoveValid(p, 0, 1, false));
     * assertTrue(board.isMoveValid(p, 1, 0, false));
     * assertFalse(board.isMoveValid(p, 1, 1, false));
     * assertFalse(board.isMoveValid(p, 0, 0, false)); // same position // test all
     * the matrix! for (int i = 1; i < board.getBoard().length; i++) { for (int j =
     * 1; j < board.getBoard()[1].length; j++) { assertFalse(board.isMoveValid(p, i,
     * j, false)); } } // CORNER 2 board.placeStone(BoardPoint.BLACK, 12, 0);
     * assertTrue(board.isMoveValid(p, 12, 1, false));
     * assertTrue(board.isMoveValid(p, 11, 0, false));
     * assertFalse(board.isMoveValid(p, 11, 1, false)); // diagonal
     * assertFalse(board.isMoveValid(p, 12, 0, false)); // same position // CORNER 3
     * board.placeStone(BoardPoint.BLACK, 0, 12); assertTrue(board.isMoveValid(p, 0,
     * 11, false)); assertTrue(board.isMoveValid(p, 1, 12, false));
     * assertFalse(board.isMoveValid(p, 1, 11, false)); // diagonal // CORNER 4
     * board.placeStone(BoardPoint.BLACK, 12, 12); assertTrue(board.isMoveValid(p,
     * 12, 11, false)); assertTrue(board.isMoveValid(p, 11, 12, false));
     * assertFalse(board.isMoveValid(p, 11, 11, false)); // diagonal // IN THE
     * MIDDLE OF THE MATRIX board.placeStone(BoardPoint.BLACK, 6, 6);
     * assertTrue(board.isMoveValid(p, 6, 5, false));
     * assertTrue(board.isMoveValid(p, 6, 7, false));
     * assertTrue(board.isMoveValid(p, 5, 6, false));
     * assertTrue(board.isMoveValid(p, 7, 6, false));
     * assertFalse(board.isMoveValid(p, 7, 7, false)); // diagonal
     * assertFalse(board.isMoveValid(p, 5, 7, false)); // diagonal
     * assertFalse(board.isMoveValid(p, 5, 5, false)); // diagonal
     * assertFalse(board.isMoveValid(p, 7, 5, false)); // diagonal }
     */
    @Test
    public void testCorrectPlaceStone() {
        Board board = new Board();
        Player player1 = new Player(BoardPoint.BLACK);
        assertSame(BoardPoint.BLACK, player1.color());
        board.placeStone(BoardPoint.BLACK, 1, 2);
        String[][] matrix1 = BoardPoint.toMatrixString(board.getBoard());
        String[][] matrix2 = {
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."}
        };
        assertTrue(areMatricesEqual(matrix1, matrix2));
    }

    @Test
    public void testIncorrectPlaceStone() {
        Board board = new Board();
        board.placeStone(BoardPoint.BLACK, 1, 2);
        String[][] matrix1 = BoardPoint.toMatrixString(board.getBoard());
        String[][] matrix2 = {
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "W", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."}
        };

        assertSame(
                matrix1[1][3],
                matrix2[1][3],
                matrix1[1][3] + " == " + matrix2[1][3]); // this is always true
        // because I'm checking the
        // single element
        assertFalse(areMatricesEqual(matrix1, matrix2));
    }

    @Test
    public void testToCompactStringAndFromCompactString() {
        String[][] matrix = {
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."},
            {".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "."}
        };
        Board board = new Board(matrix);
        String result = board.toCompactString();
        System.out.println("il risultato finale è " + result);

        Board newBoard = new Board();
        newBoard.fromCompactString(result);
        System.out.println("il risultato finale è " + newBoard.toCompactString());
        assertEquals(result, newBoard.toCompactString());
    }

    @Test
    public void testConstructorFromCompactString() {
        String matrix =
                "....B............B...W........B............B............B............B............B............B............B............B............B............B............B........";

        Board board = new Board(matrix);
        String result = board.toCompactString();
        assertEquals(matrix, result);
    }
}