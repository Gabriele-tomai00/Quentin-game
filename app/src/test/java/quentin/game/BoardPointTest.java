package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test; // Test of JUnit 5

class BoardPointTest {

    @Test
    void testToString() {
        assertAll(
                () -> assertEquals("B", BoardPoint.BLACK.toString()),
                () -> assertEquals("W", BoardPoint.WHITE.toString()),
                () -> assertEquals(".", BoardPoint.EMPTY.toString()));
    }
}
