package quentin;

public record Cell(int row, int col) {

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Cell) {
            Cell cell = (Cell) obj;
            if (cell.row == this.row && cell.col == this.col) return true;
        }
        return false;
    }

    @Override
    public final String toString() {
        // TODO Auto-generated method stub
        return String.format("(%c, %d)", row + 'a', col + 1);
    }
}
