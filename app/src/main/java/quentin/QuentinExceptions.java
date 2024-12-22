package quentin;

class InvalidCellValuesException extends Exception {

    private static final long serialVersionUID = -297542594333253705L;

    public InvalidCellValuesException(String message) {
        super(message);
    }
}

class MoveException extends Exception {

    private static final long serialVersionUID = 4207157178820508022L;

    public MoveException() {
        super();
    }

    public MoveException(String message) {
        super(message);
    }
}

class CellAlreadyTakenException extends MoveException {

    private static final long serialVersionUID = 3817524499307938631L;
    private final Cell cell;

    public CellAlreadyTakenException(Cell cell) {
        super();
        this.cell = cell;
    }

    @Override
    public String toString() {
        return String.format("Cell %s is not empty!", cell);
    }
}

class IllegalMoveException extends MoveException {

    private static final long serialVersionUID = 2693504242559053588L;

    public IllegalMoveException() {
        super();
    }

    public IllegalMoveException(String message) {
        super(message);
    }
}
