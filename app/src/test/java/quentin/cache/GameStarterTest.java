package quentin.cache;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quentin.game.BoardPoint;
import quentin.game.Cell;

class GameStarterTest {
    private ByteArrayOutputStream outputErr = new ByteArrayOutputStream();
    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    void setOutput() {
        System.setErr(new PrintStream(outputErr));
        System.setOut(new PrintStream(output));
    }

    void provideInput(String data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes());
        System.setIn(is);
    }

    @Test
    void testBlackWin() {
        StringBuilder builder = new StringBuilder();
        for (int row = 1; row <= 13; row++) {
            builder.append("a" + row + "\n");
            builder.append("c" + row + "\n");
        }
        provideInput(builder.toString());
        CachedGameStarter starter = new CachedGameStarter();
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

    @Test
    void testWrongMove() {
        String commands = "a1\na1\nexit\n";
        provideInput(commands);
        CachedGameStarter starter = new CachedGameStarter();
        starter.run();
        assertTrue(outputErr.toString().contains("Cell (a, 1) is not empty!"));
    }

    @Test
    void testBackAndForward() {
        String commands = "a1\na2\nback\nforward\nexit\n";
        provideInput(commands);
        CachedGameStarter starter = new CachedGameStarter();
        starter.run();
        assertEquals(BoardPoint.WHITE, starter.getGame().getBoard().getPoint(new Cell(1, 0)));
    }

    @Test
    void testHelper() {
        CachedGameStarter starter = new CachedGameStarter();
        starter.showHelper();
        assertAll(
                () ->
                        assertTrue(
                                output.toString()
                                        .contains("back or b                : go back one move")),
                () ->
                        assertTrue(
                                output.toString()
                                        .contains(
                                                "forward or f             : go forward one move")));
    }
}
