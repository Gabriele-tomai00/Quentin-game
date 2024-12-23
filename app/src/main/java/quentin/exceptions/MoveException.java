package quentin.exceptions;

import quentin.Cell;

public class MoveException extends RuntimeException {

    private static final long serialVersionUID = 4207157178820508022L;
    private final Cell cell;

    public MoveException(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }
}
