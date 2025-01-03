package quentin.exceptions;

public class IllegalBoardException extends RuntimeException {

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
