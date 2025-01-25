package quentin.game;

// JUnit 5
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void assignationTest() {
        Player playerBlack = new Player(BoardPoint.BLACK);
        Player playerWhite = new Player(BoardPoint.WHITE);

        assertEquals(BoardPoint.BLACK, playerBlack.color());
        assertEquals(BoardPoint.WHITE, playerWhite.color());
    }
}
