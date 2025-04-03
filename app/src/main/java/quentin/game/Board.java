package quentin.game;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class Board implements Serializable {

    @Serial private static final long serialVersionUID = 8169137628862217460L;
    private static final int SIZE = 13;
    private BoardPoint[][] gameBoard = new BoardPoint[SIZE][SIZE];

    public Board() {
        for (BoardPoint[] row : gameBoard) {
            Arrays.fill(row, BoardPoint.EMPTY);
        }
    }

    public BoardPoint[][] getBoard() {
        return Arrays.copyOf(gameBoard, SIZE);
    }

    public BoardPoint getPoint(Cell cell) {
        return gameBoard[cell.row()][cell.col()];
    }

    public void placeStone(BoardPoint stone, int row, int col) {
        gameBoard[row][col] = stone;
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
        if (gameBoard[i][j].equals(BoardPoint.WHITE)) toReturn.append(" ██ ");
        else if (gameBoard[i][j].equals(BoardPoint.BLACK)) toReturn.append(" XX ");
        else if (gameBoard[i][j].equals(BoardPoint.EMPTY)) toReturn.append("    ");
        if (j == SIZE - 1) toReturn.append("│█");
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
        if (obj instanceof Board bd) {
            return Arrays.deepEquals(this.gameBoard, bd.gameBoard);
        }
        return false;
    }

    public void setBoard(Board board) {
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(board.getBoard()[row], 0, this.gameBoard[row], 0, SIZE);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getBoard());
    }
}
