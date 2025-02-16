package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CellTest {

  @Test
  void cellsAreEqualTest() {
    assertAll(() -> assertEquals(new Cell(0, 0), new Cell(0, 0)),
              () -> assertEquals(new Cell(2, 3), new Cell(2, 3)),
              () -> assertNotEquals(new Cell(0, 0), new Cell(2, 5)));
  }

  @Test
  void cellsHashAreTheSameTest() {
    assertAll(() -> assertEquals(new Cell(0, 0), new Cell(0, 0)), () -> assertEquals(new Cell(10, 3), new Cell(10, 3)));
  }
}
