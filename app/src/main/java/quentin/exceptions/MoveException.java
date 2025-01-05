package quentin.exceptions;

import java.io.Serial;
import quentin.game.Cell;

public class MoveException extends RuntimeException {

    @Serial private static final long serialVersionUID = 4207157178820508022L;
    private final Cell cell;

    public MoveException(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }
}
