package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class BoardPointTest {

    @Test
    public void testFromString() {
        assertAll(
                () -> assertEquals(BoardPoint.BLACK, BoardPoint.fromString("B")),
                () -> assertEquals(BoardPoint.BLACK, BoardPoint.fromString("Black")),
                () -> assertEquals(BoardPoint.WHITE, BoardPoint.fromString("W")),
                () -> assertEquals(BoardPoint.WHITE, BoardPoint.fromString("White")),
                () -> assertEquals(BoardPoint.EMPTY, BoardPoint.fromString(".")),
                () -> assertEquals(BoardPoint.EMPTY, BoardPoint.fromString("Unknown")));
    }

    @Test
    public void testToString() {
        assertAll(
                () -> assertEquals("B", BoardPoint.BLACK.toString()),
                () -> assertEquals("W", BoardPoint.WHITE.toString()),
                () -> assertEquals(".", BoardPoint.EMPTY.toString()));
    }
}
