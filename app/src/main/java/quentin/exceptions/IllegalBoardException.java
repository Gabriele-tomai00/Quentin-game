package quentin.exceptions;

import java.io.Serial;

public class IllegalBoardException extends RuntimeException {

    @Serial private static final long serialVersionUID = -5208454396928964658L;
    private final String message;

    public IllegalBoardException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
