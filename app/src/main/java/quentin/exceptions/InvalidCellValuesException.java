package quentin.exceptions;

import java.io.Serial;

public class InvalidCellValuesException extends RuntimeException {

    @Serial private static final long serialVersionUID = -297542594333253705L;

    public InvalidCellValuesException(String message) {
        super(message);
    }
}
