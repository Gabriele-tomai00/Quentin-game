package quentin.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.Player;

class NetworkHandlerTest {

    FakeSocket fakeSocket;
    OnlineGame game = new OnlineGame(new Player(BoardPoint.WHITE));
    NetworkHandler handler;
    PrintWriter pw;
    private BufferedReader br;

    @BeforeEach
    void createHandler() throws IOException {
        fakeSocket = new FakeSocket();
        handler = new NetworkHandler(fakeSocket, game);
        pw = new PrintWriter(fakeSocket.getOutputStream(), true);
        br = new BufferedReader(new InputStreamReader(fakeSocket.getInputStream()));
    }

    @Test
    void testPieRule() {
        String commands = "pie\nexit";
        // new Thread(handler).start();
        pw.println(commands);
        handler.run();
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
    }

    @Test
    void testPlayOnce() {
        String commands = "a1\nexit";
        pw.println(commands);
        handler.run();
        BoardPoint cell = game.getBoard().getPoint(new Cell(0, 0));
        assertEquals(BoardPoint.WHITE, cell);
    }

    @Test
    void testSendCommands() throws IOException {
        handler.sendCommands("pie");
        String message = br.readLine();
        assertEquals("pie", message);
    }
}
