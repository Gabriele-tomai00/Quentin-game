package quentin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import quentin.game.Cell;

class CellTest {

    @Test
    void cellsAreEqual() {
        assertAll(
                "Verify if cells are the same",
                () -> assertEquals(new Cell(0, 0), new Cell(0, 0)),
                () -> assertEquals(new Cell(2, 3), new Cell(2, 3)));
    }

    @Test
    void cellsHashAreTheSame() {
        assertAll(
                "Verify if the set contains these cells",
                () -> assertEquals(new Cell(0, 0), new Cell(0, 0)),
                () -> assertEquals(new Cell(10, 3), new Cell(10, 3)));
    }
}
