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
import quentin.game.OnlineGame;
import quentin.game.OnlineGameStarter;

class OnlineGameStarterTest {

    private ByteArrayOutputStream outputErr = new ByteArrayOutputStream();
    private NetworkHandler fakeHandler =
            new NetworkHandler(null, new OnlineGame(BoardPoint.BLACK)) {
                @Override
                public synchronized void sendCommands(CommunicationProtocol command) {
                    // do nothing
                }
            };

    @BeforeEach
    void setOutput() {
        System.setErr(new PrintStream(outputErr));
    }

    @Test
    void testPieRule() {
        String commands = "pie\nexit\n";
        StreamUtility.provideInput(commands);
        OnlineGameStarter starter =
                new OnlineGameStarter(fakeHandler, new OnlineGame(BoardPoint.WHITE));
        starter.run();
        BoardPoint color = starter.getGame().getCurrentPlayer().color();
        assertEquals(BoardPoint.BLACK, color);
    }

    @Test
    void testPieRuleNotPossible() {
        String commands = "pie\nexit\n";
        StreamUtility.provideInput(commands);
        OnlineGameStarter starter =
                new OnlineGameStarter(fakeHandler, new OnlineGame(BoardPoint.BLACK));
        starter.run();
        BoardPoint color = starter.getGame().getCurrentPlayer().color();
        String errorMessage = outputErr.toString();
        assertAll(
                () -> assertEquals(BoardPoint.BLACK, color),
                () -> assertTrue(errorMessage.contains("Cannot apply pie rule!")));
    }

    @Test
    void placeOneTileTest() {
        String commands = "a1\nexit\n";
        StreamUtility.provideInput(commands);
        OnlineGameStarter starter =
                new OnlineGameStarter(fakeHandler, new OnlineGame(BoardPoint.BLACK));
        starter.run();
        BoardPoint cell = BoardPoint.BLACK;
        assertEquals(cell, starter.getGame().getBoard().getPoint(new Cell(0, 0)));
    }
}
