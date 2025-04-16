package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testEquals() {
        Player black1 = new Player(BoardPoint.BLACK);
        Player black2 = new Player(BoardPoint.BLACK);
        Player white = new Player(BoardPoint.WHITE);
        assertAll(() -> assertEquals(black1, black2), () -> assertNotEquals(black1, white));
    }

    @Test
    void testToString() {
        Player white = new Player(BoardPoint.WHITE);
        assertEquals("White", white.toString());
    }
}
