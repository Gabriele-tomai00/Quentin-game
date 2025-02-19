package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

class SimpleGameStarterTest {

    void provideInput(String data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes());
        System.setIn(is);
    }

    /*
     * @Test void testExit() { provideInput("exit\n"); GameStarter starter = new
     * CachedGameStarter(); starter.start(); assertTrue(true); }
     */

    @Test
    void testBlackWin() {
        StringBuilder builder = new StringBuilder();
        for (int row = 1; row <= 13; row++) {
            builder.append("a" + row + "\n");
            builder.append("c" + row + "\n");
        }
        provideInput(builder.toString());
        GameStarter starter = new GameStarter();
        starter.run();
        assertAll(
                () ->
                        assertEquals(
                                BoardPoint.BLACK,
                                starter.getGame().getBoard().getPoint(new Cell(0, 0))),
                () ->
                        assertEquals(
                                BoardPoint.BLACK,
                                starter.getGame().getBoard().getPoint(new Cell(12, 0))),
                () -> assertTrue(starter.hasWon()));
    }
}
