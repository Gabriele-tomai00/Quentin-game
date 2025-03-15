package quentin.network;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.OnlineGame;
import quentin.game.Player;

class NetworkHandlerTest {

    FakeSocket fakeSocket;
    OnlineGame game = new OnlineGame(new Player(BoardPoint.WHITE));
    NetworkHandler handler;
    PrintWriter pw;
    BufferedReader br;

    @BeforeEach
    void createHandler() throws IOException {
        fakeSocket = new FakeSocket();
        handler = new NetworkHandler(fakeSocket, game);
        pw = new PrintWriter(fakeSocket.getOutputStream(), true);
        br = new BufferedReader(new InputStreamReader(fakeSocket.getInputStream()));
    }

    @Test
    void testPieRule() throws InterruptedException {
        String commands = "pie\nexit";
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(
                        () -> {
                            handler.run();
                            latch.countDown();
                        })
                .start();
        pw.println(commands);
        boolean completed = latch.await(2, TimeUnit.SECONDS);
        assertTrue(completed, "Handler did not complete in time");
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
    }

    @Test
    void testPlayOnce() throws InterruptedException {
        String commands = "a1\nexit";
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(
                        () -> {
                            handler.run();
                            latch.countDown();
                        })
                .start();
        pw.println(commands);
        boolean completed = latch.await(2, TimeUnit.SECONDS);

        BoardPoint cell = game.getBoard().getPoint(new Cell(0, 0));
        assertTrue(completed, "Handler did not complete in time");
        assertEquals(BoardPoint.WHITE, cell);
    }

    @Test
    void testSendCommands() throws IOException, InterruptedException {
        String commands = "a1\nexit";
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(
                        () -> {
                            handler.run();
                            latch.countDown();
                        })
                .start();
        pw.println(commands);
        boolean await = latch.await(2, TimeUnit.SECONDS);
        handler.sendCommands("pie");
        //    String message = br.readLine();
        assertAll(
                () -> assertTrue(handler.isWaiting()),
                () -> assertTrue(await, "Handler did not complete in time"));
        //              () -> assertEquals("pie", message));
    }
}
