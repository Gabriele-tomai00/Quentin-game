package quentin;

class InvalidCellValuesException extends RuntimeException {

    private static final long serialVersionUID = -297542594333253705L;

    public InvalidCellValuesException(String message) {
        super(message);
    }
}

class MoveException extends RuntimeException {

    private static final long serialVersionUID = 4207157178820508022L;
    private final Cell cell;

    public MoveException(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }
}

class CellAlreadyTakenException extends MoveException {

    private static final long serialVersionUID = 3817524499307938631L;

    public CellAlreadyTakenException(Cell cell) {
        super(cell);
    }

    @Override
    public String getMessage() {
        return String.format("Cell %s is not empty!", getCell());
    }
}

class IllegalMoveException extends MoveException {

    private static final long serialVersionUID = 2693504242559053588L;

    public IllegalMoveException(Cell cell) {
        super(cell);
    }

    @Override
    public String getMessage() {
        return String.format(
                "Cell %s, is not connected to other cells of the same color!", getCell());
    }
}
