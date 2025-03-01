package quentin.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import quentin.exceptions.IllegalBoardException;

public class GameBoard implements Serializable {

    @Serial private static final long serialVersionUID = 8169137628862217460L;
    private static final int SIZE = 13;
    private BoardPoint[][] board = new BoardPoint[SIZE][SIZE];

    public GameBoard() {
        for (BoardPoint[] row : board) {
            Arrays.fill(row, BoardPoint.EMPTY);
        }
    }

    public GameBoard(String compactBoard) {
        fromCompactString(compactBoard);
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
                appendBoardPoint(i, j, toReturn);
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

    public void appendBoardPoint(int i, int j, StringBuilder toReturn) {
        toReturn.append("│");
        if (board[i][j].equals(BoardPoint.WHITE)) toReturn.append(" ██ ");
        else if (board[i][j].equals(BoardPoint.BLACK)) toReturn.append(" XX ");
        else if (board[i][j].equals(BoardPoint.EMPTY)) toReturn.append("    ");
        if (j == SIZE - 1) toReturn.append("│█");
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
        int i = 0;
        int flattenedIndex = 0;
        while (i < compactString.length()) {
            char ch = compactString.charAt(i);
            if (Character.isDigit(ch)) {
                int start = i;
                while (i < compactString.length() && Character.isDigit(compactString.charAt(i))) {
                    i++;
                }
                flattenedIndex = fillBoardWithEmptyCells(compactString, flattenedIndex, start, i);

            } else {
                if (flattenedIndex >= SIZE * SIZE) {
                    throw new IllegalBoardException("Compact string exceeds board size");
                }
                this.board[flattenedIndex / SIZE][flattenedIndex % SIZE] =
                        BoardPoint.fromString(String.valueOf(ch));
                flattenedIndex++;
                i++;
            }
        }
        if (flattenedIndex != SIZE * SIZE) {
            throw new IllegalBoardException("Compact string does not match the board size.");
        }
    }

    public int fillBoardWithEmptyCells(String compactString, int flattenedIndex, int start, int i) {
        int count = Integer.parseInt(compactString.substring(start, i));
        for (int j = 0; j < count; j++) {
            if (flattenedIndex >= SIZE * SIZE) {
                throw new IllegalBoardException("Compact string exceeds board size");
            }
            this.board[flattenedIndex / SIZE][flattenedIndex % SIZE] = BoardPoint.EMPTY;
            flattenedIndex++;
        }
        return flattenedIndex;
    }

    public int size() {
        return SIZE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof GameBoard bd) {
            return Arrays.deepEquals(this.board, bd.board);
        }
        return false;
    }

    public void setBoard(GameBoard board) {
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(board.getBoard()[row], 0, this.board[row], 0, SIZE);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getBoard());
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
