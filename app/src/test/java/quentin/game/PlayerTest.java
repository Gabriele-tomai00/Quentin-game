package quentin.game;

// JUnit 5
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testBlack() {
        Player playerBlack = new Player(BoardPoint.BLACK);
        assertEquals(BoardPoint.BLACK, playerBlack.color());
    }

    @Test
    void testWhite() {
        Player playerWhite = new Player(BoardPoint.WHITE);
        assertEquals(BoardPoint.WHITE, playerWhite.color());
    }
}
