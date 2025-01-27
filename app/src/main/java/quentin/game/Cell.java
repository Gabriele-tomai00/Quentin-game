package quentin.game;

import java.util.Objects;

public record Cell(int row, int col) {

  @Override
  public int hashCode() {
    return Objects.hash(col, row);
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj instanceof Cell(int row, int col)) { return row == row() && col == col(); }
    return false;
  }

  @Override
  public final String toString() {
    return String.format("(%c, %d)", col + 'a', row + 1);
  }
}
