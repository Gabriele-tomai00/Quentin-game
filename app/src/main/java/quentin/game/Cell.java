package quentin.game;

import java.util.Objects;

public record Cell(int row, int col) {

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Cell(int otherRow, int otherCol)) {
            return row == otherRow && col == otherCol;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("(%c, %d)", col + 'a', row + 1);
    }
}
