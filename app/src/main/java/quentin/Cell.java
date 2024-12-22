package quentin;

import java.util.Objects;

public class Cell {

  private int row, col;

  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int row() {
    return row;
  }

  public int col() {
    return col;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public void setCol(int col) {
    this.col = col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(col, row);
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof Cell) {
      Cell cell = (Cell) obj;
      return cell.row == this.row && cell.col == this.col;
    }
    return false;
  }

  @Override
  public final String toString() {
    // TODO Auto-generated method stub
    return String.format("(%c, %d)", row + 'a', col + 1);
  }
}
