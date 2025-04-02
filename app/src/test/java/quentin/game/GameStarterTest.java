package quentin.game;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameStarterTest {

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private GameStarter starter = new GameStarter();

    @BeforeEach
    void changeOutput() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testHelper() {
        starter.showHelper();
        assertAll(
                () -> assertTrue(outContent.toString().contains("Available commands:\n")),
                () ->
                        assertTrue(
                                outContent
                                        .toString()
                                        .contains(
                                                "  exit                     : Quits the game and"
                                                        + " exits the program    \n")),
                () ->
                        assertTrue(
                                outContent
                                        .toString()
                                        .contains(
                                                "  help                     : Shows this help      "
                                                        + "                   \n")),
                () ->
                        assertTrue(
                                outContent
                                        .toString()
                                        .contains(
                                                "  <coordinates>            : Makes a move."
                                                        + " Examples: A1 b2 C5 (wrong examples: 5A,"
                                                        + " 24)")));
    }
}
