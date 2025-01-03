package quentin.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import quentin.exceptions.IllegalBoardException;

public class Board implements Serializable {

    @Serial private static final long serialVersionUID = 8169137628862217460L;
    private static final int SIZE = 13;
    private BoardPoint[][] board = new BoardPoint[SIZE][SIZE];

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
        return Arrays.copyOf(board, SIZE);
    }

    public BoardPoint getPoint(Cell cell) {
        return board[cell.row()][cell.col()];
    }

    public void placeStone(BoardPoint stone, int row, int col) {
        board[row][col] = stone;
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
                    if (i + 1 > 9) toReturn.append(i + 1).append("   █");
                    else toReturn.append(i + 1).append("    █");
                }
                toReturn.append("│");
                if (board[i][j].equals(BoardPoint.WHITE)) toReturn.append(" ██ ");
                else if (board[i][j].equals(BoardPoint.BLACK)) toReturn.append(" XX ");
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
                        throw new IllegalBoardException(
                                "Compact string exceeds board size", compactString);
                    }
                    this.board[index / SIZE][index % SIZE] = BoardPoint.EMPTY;
                    index++;
                }
            } else {
                if (index >= SIZE * SIZE) {
                    throw new IllegalBoardException(
                            "Compact string exceeds board size", compactString);
                }
                this.board[index / SIZE][index % SIZE] = BoardPoint.fromString(String.valueOf(ch));
                i++;
                index++;
            }
        }
        if (index != SIZE * SIZE) {
            throw new IllegalBoardException(
                    "Compact string does not match the board size.", compactString);
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
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Board board) {
            return Arrays.deepEquals(this.board, board.board);
        }
        return false;
    }

    public void setBoard(Board board) {
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(board.getBoard()[row], 0, this.board[row], 0, SIZE);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(this.toCompactString());
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        String compactString = (String) ois.readObject();
        board = new BoardPoint[SIZE][SIZE];
        this.fromCompactString(compactString);
    }
}
