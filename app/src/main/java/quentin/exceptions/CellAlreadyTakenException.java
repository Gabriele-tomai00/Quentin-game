package quentin.exceptions;

import quentin.Cell;

public class CellAlreadyTakenException extends MoveException {

    private static final long serialVersionUID = 3817524499307938631L;

    public CellAlreadyTakenException(Cell cell) {
        super(cell);
    }

    @Override
    public String getMessage() {
        return String.format("Cell %s is not empty!", getCell());
    }
}
