package quentin.network;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quentin.StreamUtility;

class NetworkStarterTest {
    private ByteArrayOutputStream outputErr = new ByteArrayOutputStream();
    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Test
    void testRun() {
        String input = "getu\ngetp\nexit\n";
        StreamUtility.provideInput(input);
        NetworkStarter starter = new NetworkStarter();
        starter.run();
        String messages = output.toString();
        assertTrue(messages.contains("6789"));
    }

    @Test
    void changePortTest() {
        StreamUtility.provideInput("setport\n5000\nexit\n");
        NetworkStarter starter = new NetworkStarter();
        starter.run();
        String changePortMessage = output.toString();
        assertAll(
                () -> assertTrue(changePortMessage.contains("Enter new port:")),
                () -> assertEquals(5000, starter.getHandler().getPort()));
    }

    @Test
    void changeUsernameTest() {
        StreamUtility.provideInput("setu\ngabri\nexit\n");
        NetworkStarter starter = new NetworkStarter();
        starter.run();
        String changePortMessage = output.toString();
        assertAll(
                () -> assertTrue(changePortMessage.contains("Enter username:")),
                () -> assertEquals("gabri", starter.getHandler().getUsername()));
    }

    @Test
    void testHelper() {
        StreamUtility.provideInput("help\nexit\n");
        NetworkStarter starter = new NetworkStarter();
        starter.run();
        String helperMessage = output.toString();
        assertAll(
                () -> assertTrue(helperMessage.contains("Available commands:\n")),
                () ->
                        assertTrue(
                                helperMessage.contains(
                                        "  exit                     : Quits the game and exits the"
                                                + " program    \n")),
                () ->
                        assertTrue(
                                helperMessage.contains(
                                        "  help                     : Shows this help")),
                () ->
                        assertTrue(
                                helperMessage.contains(
                                        "setusername              : Set a username to be recognized"
                                                + " by other players when playing online")),
                () ->
                        assertTrue(
                                helperMessage.contains(
                                        "setport                  : Set a new port, in case it is"
                                            + " already used (IMPORTANT: the other player must know"
                                            + " the new port)")),
                () ->
                        assertTrue(
                                helperMessage.contains(
                                        "getusername or getu      : Prints you current username")),
                () ->
                        assertTrue(
                                helperMessage.contains(
                                        "getport or getp          : Prints the currently used TCP"
                                                + " port (your opposing player may need it)")),
                () ->
                        assertTrue(
                                helperMessage.contains(
                                        "startserver or ss        : Starts a server to play with"
                                                + " other players")),
                () ->
                        assertTrue(
                                helperMessage.contains(
                                        "startclient or sc        : Starts a client to play with"
                                                + " other players")));
    }

    @BeforeEach
    void setOutput() {
        System.setErr(new PrintStream(outputErr));
        System.setOut(new PrintStream(output));
    }
}
