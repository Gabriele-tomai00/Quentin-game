package quentin.exceptions;

import java.io.Serial;

import quentin.game.Cell;

public class CellAlreadyTakenException extends MoveException {

    @Serial private static final long serialVersionUID = 3817524499307938631L;

    public CellAlreadyTakenException(Cell cell) {
        super(cell);
    }

    @Override
    public String getMessage() {
        return String.format("Cell %s is not empty!%n", getCell());
    }
}
