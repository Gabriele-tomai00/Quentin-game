package quentin.network;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quentin.StreamUtility;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.Player;

class OnlineGameStarterTest {

    private ByteArrayOutputStream outputErr = new ByteArrayOutputStream();
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private NetworkHandler fakeHandler =
            new NetworkHandler(null, null) {
                @Override
                public synchronized void sendCommands(String command) {
                    // do nothing
                }
            };

    @BeforeEach
    void setOutput() {
        System.setErr(new PrintStream(outputErr));
        System.setOut(new PrintStream(output));
    }

    @Test
    void testPieRule() {
        String commands = "pie\nexit\n";
        StreamUtility.provideInput(commands);
        OnlineGameStarter starter =
                new OnlineGameStarter(fakeHandler, new OnlineGame(new Player(BoardPoint.WHITE)));
        starter.run();
        BoardPoint color = starter.getGame().getCurrentPlayer().color();
        assertEquals(BoardPoint.BLACK, color);
    }

    @Test
    void testPieRuleNotPossible() {
        String commands = "pie\nexit\n";
        StreamUtility.provideInput(commands);
        OnlineGameStarter starter =
                new OnlineGameStarter(fakeHandler, new OnlineGame(new Player(BoardPoint.BLACK)));
        starter.run();
        BoardPoint color = starter.getGame().getCurrentPlayer().color();
        String errorMessage = "Cannot apply pie rule!";
        assertAll(
                () -> assertEquals(BoardPoint.BLACK, color),
                () -> assertTrue(outputErr.toString().contains(errorMessage)));
    }

    @Test
    void placeOneTileTest() {
        String commands = "a1\nexit\n";
        StreamUtility.provideInput(commands);
        OnlineGameStarter starter =
                new OnlineGameStarter(fakeHandler, new OnlineGame(new Player(BoardPoint.BLACK)));
        starter.run();
        BoardPoint cell = BoardPoint.BLACK;
        assertEquals(cell, starter.getGame().getBoard().getPoint(new Cell(0, 0)));
    }
}
