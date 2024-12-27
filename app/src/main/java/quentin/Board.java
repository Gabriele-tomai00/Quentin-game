package quentin;

import java.util.Arrays;

public class Board {

    public final int SIZE = 13; // Board size (specified in rules)
    private final BoardPoint[][] board = new BoardPoint[SIZE][SIZE];
    private final boolean[][] visited = new boolean[SIZE][SIZE];

    public Board() {
        for (BoardPoint[] row : board) {
            Arrays.fill(row, BoardPoint.EMPTY);
        }
    }

    // second constructor for testing
    public Board(String[][] initialBoard) {
        if (initialBoard.length != SIZE || initialBoard[0].length != SIZE) {
            throw new IllegalArgumentException(
                    "Matrix size must be: "
                            + SIZE
                            + "x"
                            + SIZE
                            + ". Your matrix is "
                            + initialBoard.length
                            + "x"
                            + initialBoard[0].length);
        }

        // this.board = new BoardPoint[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                visited[i][j] = false;
                String cell = initialBoard[i][j];
                if (!cell.equals(".") && !cell.equals("B") && !cell.equals("W")) {
                    throw new IllegalArgumentException(
                            "Not valid value in matrix: ("
                                    + cell
                                    + " in "
                                    + j
                                    + ","
                                    + i
                                    + " position). The only allowed values are '.', 'B' and 'W'");
                }
                this.board[i][j] = BoardPoint.fromString(cell);
            }
        }
    }

    public Board(String compactString) {
        this();
        this.fromCompactString(compactString);
    }

    public BoardPoint[][] getBoard() {
        return board;
    }

    public BoardPoint getPoint(Cell cell) {
        return board[cell.row()][cell.col()];
    }

    public BoardPoint getValues(int i, int j) {
        if (i < SIZE && j < SIZE) return board[i][j];
        else throw new RuntimeException("Coordinates not valid");
    }

    public void placeStone(BoardPoint stone, int row, int col) {
        board[row][col] = stone;
    }

    public boolean hasWon(BoardPoint color) {
        for (boolean[] row : visited) {
            Arrays.fill(row, false);
        }
        if (color == BoardPoint.WHITE) {
            for (int i = 0; i < SIZE; i++) if (findWinnerPath(BoardPoint.WHITE, i, 0)) return true;
        } else if (color == BoardPoint.BLACK) {
            for (int j = 0; j < SIZE; j++) if (findWinnerPath(BoardPoint.BLACK, 0, j)) return true;
        } else throw new RuntimeException("Not valid color");
        return false;
    }

    public boolean findWinnerPath(BoardPoint color, int i, int j) {
        if (visited[i][j] || board[i][j] != color) {
            return false;
        }
        visited[i][j] = true;

        // orthogonal direction
        // right
        if (j + 1 < SIZE && findWinnerPath(color, i, j + 1)) {
            return true;
        }
        // left
        if (j - 1 >= 0 && findWinnerPath(color, i, j - 1)) {
            return true;
        }
        // up
        if (i - 1 >= 0 && findWinnerPath(color, i - 1, j)) {
            return true;
        }
        // down
        if (i + 1 < SIZE && findWinnerPath(color, i + 1, j)) {
            return true;
        }

        return color == BoardPoint.WHITE ? j == SIZE - 1 : i == SIZE - 1;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("        A    B    C    D    E    F    G    H    I    J    K    L    M\n");
        toReturn.append(
                "      ┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐  \n");

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (j == 0) {
                    if (i > 9) toReturn.append(i).append("   █");
                    else toReturn.append(i).append("    █");
                }
                toReturn.append("│");
                if (board[i][j].equals(BoardPoint.WHITE)) toReturn.append(" ██ ");
                else if (board[i][j].equals(BoardPoint.BLACK)) toReturn.append(" xx ");
                else if (board[i][j].equals(BoardPoint.EMPTY)) toReturn.append("    ");

                if (j == SIZE - 1) toReturn.append("│█");
            }
            toReturn.append("\n");
            if (i == SIZE - 1) {
                toReturn.append(
                        "      └────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘ "
                                + " \n");
            } else {
                toReturn.append(
                        "     █├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤█\n");
            }
        }

        return toReturn.toString();
    }

    public String toCompactString() {
        StringBuilder result = new StringBuilder();
        int dotCount = 0;

        for (BoardPoint[] row : board) {
            for (BoardPoint point : row) {
                String str = point.toString();
                if (".".equals(str)) {
                    dotCount++;
                } else {
                    if (dotCount > 0) {
                        result.append(dotCount);
                        dotCount = 0;
                    }
                    result.append(str);
                }
            }
        }
        if (dotCount > 0) {
            result.append(dotCount);
        }
        return result.toString();
    }

    public void fromCompactString(String compactString) {
        int index = 0; // Indice globale per iterare sulla matrice
        int length = compactString.length();

        for (int i = 0; i < length; ) { // Nota: incremento `i` manualmente
            char ch = compactString.charAt(i);
            if (Character.isDigit(ch)) {
                int start = i;
                while (i < length && Character.isDigit(compactString.charAt(i))) {
                    i++;
                }
                int count = Integer.parseInt(compactString.substring(start, i));
                for (int j = 0; j < count; j++) {
                    if (index >= SIZE * SIZE) {
                        throw new IllegalArgumentException("Compact string exceeds board size.");
                    }
                    this.board[index / SIZE][index % SIZE] = BoardPoint.EMPTY;
                    index++;
                }
            } else {
                if (index >= SIZE * SIZE) {
                    throw new IllegalArgumentException("Compact string exceeds board size.");
                }
                this.board[index / SIZE][index % SIZE] = BoardPoint.fromString(String.valueOf(ch));
                i++;
                index++;
            }
        }
        if (index != SIZE * SIZE) {
            throw new IllegalArgumentException("Compact string does not match the board size.");
        }
    }

    public int size() {
        return SIZE;
    }

    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = BoardPoint.EMPTY;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Board board2) {
            return Arrays.deepEquals(this.board, board2.board);
        }
        return false;
    }
}
