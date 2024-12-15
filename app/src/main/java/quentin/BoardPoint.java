/* (C)2024 */
package quentin;

public enum BoardPoint {
    BLACK,
    WHITE,
    EMPTY;

    @Override
    public String toString() { // necessary to print the stone in the board
        return switch (this) {
            case BLACK -> "B";
            case WHITE -> "W";
            default -> ".";
        };
    }

    public static String[][] toMatrixString(BoardPoint[][] board) {
        String[][] matrix = new String[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                matrix[i][j] = board[i][j].toString();
            }
        }
        return matrix;
    }

    public static BoardPoint fromString(String symbol) {
        return switch (symbol) {
            case "B", "Black" -> BoardPoint.BLACK;
            case "W", "White" -> BoardPoint.WHITE;
            default -> BoardPoint.EMPTY;
        };
    }

    public static BoardPoint[][] fromMatrixString(String[][] board) {
        BoardPoint[][] matrix = new BoardPoint[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                matrix[i][j] = fromString(board[i][j]);
            }
        }
        return matrix;
    }
}
