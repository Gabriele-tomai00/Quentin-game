package quentin.exceptions;

public class InvalidCellValuesException extends RuntimeException {

    private static final long serialVersionUID = -297542594333253705L;

    public InvalidCellValuesException(String message) {
        super(message);
    }
}
