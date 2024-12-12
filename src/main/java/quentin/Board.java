package quentin;

import java.util.Arrays;

public class Board {

  private final int SIZE = 13; // Board size (specified in rules)
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

  public BoardPoint[][] getBoard() {
    return Arrays.copyOf(board, SIZE);
  }

  public BoardPoint getPoint(Cell pos) {
    return board[pos.row()][pos.col()];
  }

  public int size() {
    return SIZE;
  }

  public BoardPoint getValues(int i, int j) {
    if (i < SIZE && j < SIZE) return board[i][j];
    else throw new RuntimeException("Coordinates not valid");
  }

  public boolean isMoveValid(Player player, int row, int col, boolean isFirstMove) {
    // check if the cell is valid: inside the board
    if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
      if (board[row][col] == BoardPoint.WHITE || board[row][col] == BoardPoint.BLACK) return false;

      if (!isFirstMove) {
        // check if the stone is orthogonally close another stone with
        // the same color
        try {
          if (board[row - 1][col] == player.color()) return true;
        } catch (ArrayIndexOutOfBoundsException e) { // Ignore exception
          // and continue
          // execution
        }
        try {
          if (board[row][col - 1] == player.color()) return true;
        } catch (ArrayIndexOutOfBoundsException e) { // Ignore exception
          // and continue
          // execution
        }
        try {
          if (board[row + 1][col] == player.color()) return true;
        } catch (ArrayIndexOutOfBoundsException e) { // Ignore exception
          // and continue
          // execution
        }
        try {
          if (board[row][col + 1] == player.color()) return true;
        } catch (ArrayIndexOutOfBoundsException e) { // Ignore exception
          // and continue
          // execution
        }
      } else return true;
    }

    return false;
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
    toReturn.append("          0     1    2    3   4     5    6    7    8    9    10  11  12\n");
    toReturn.append("             B     B    B    B   B     B    B    B    B    B    B   B\n");
    toReturn.append(
        "     W  ┌────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐ W\n");

    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        if (j == 0) {
          if (i > 9) toReturn.append(i).append("      ");
          else toReturn.append(i).append("       ");
        }
        toReturn.append("│");
        if (board[i][j].equals(BoardPoint.EMPTY)) toReturn.append("    ");
        else toReturn.append(" ").append(board[i][j]).append("  ");

        if (j == SIZE - 1) toReturn.append("│");
      }
      toReturn.append("\n");
      if (i == SIZE - 1) {
        toReturn.append(
            "     W  └────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘ W\n");
      } else {
        toReturn.append(
            "     W  ├────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤ W\n");
      }
    }
    toReturn.append("          B     B    B    B   B     B    B    B    B    B    B   B     B\n");

    return toReturn.toString();
  }
}
