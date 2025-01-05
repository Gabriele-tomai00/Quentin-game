package quentin.exceptions;

import java.io.Serial;
import quentin.game.Cell;

public class IllegalMoveException extends MoveException {

    @Serial private static final long serialVersionUID = 2693504242559053588L;

    public IllegalMoveException(Cell cell) {
        super(cell);
    }

    @Override
    public String getMessage() {
        return String.format(
                "Cell %s, is not connected to other cells of the same color!", getCell());
    }
}
