package quentin.exceptions;

public class IllegalBoardException extends RuntimeException {

    private static final long serialVersionUID = -5208454396928964658L;
    String message;
    String compactBoard;

    public IllegalBoardException(String message, String compactBoard) {
        this.compactBoard = compactBoard;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
