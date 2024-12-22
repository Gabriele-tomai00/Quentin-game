package quentin;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class PlayerTest {

    @Test
    public void correct() {
        Player playerBlack = new Player(BoardPoint.BLACK);
        Player playerWhite = new Player(BoardPoint.WHITE);

        assertEquals(BoardPoint.BLACK, playerBlack.color());
        assertEquals(BoardPoint.WHITE, playerWhite.color());
    }

    @Test
    public void incorrect() { // EMPTY is not allowed color
        Exception exception =
                assertThrows(IllegalArgumentException.class, () -> new Player(BoardPoint.EMPTY));
        assertEquals(
                "Error during player initialization, empty color not allowed",
                exception.getMessage());
    }
}
