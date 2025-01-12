package quentin.game;

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
        if (this == obj) {
            return true;
        }
        if (obj instanceof Cell cell) {
            return cell.row == this.row && cell.col == this.col;
        }
        return false;
    }

    @Override
    public final String toString() {
        return String.format("(%c, %d)", col + 'a', row + 1);
    }
}
