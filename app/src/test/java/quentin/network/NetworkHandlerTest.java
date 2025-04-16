package quentin.network;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.OnlineGame;

class NetworkHandlerTest {

    private OnlineGame game = new OnlineGame(BoardPoint.WHITE);
    private NetworkHandler handler;
    private PrintWriter pw;

    @Test
    void testPieRule() throws InterruptedException {
        CommunicationProtocol pie = CommunicationProtocol.pie();
        CommunicationProtocol exit = CommunicationProtocol.exit();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(
                        () -> {
                            handler.run();
                            latch.countDown();
                        })
                .start();
        pw.println(pie);
        pw.println(exit);
        boolean completed = latch.await(2, TimeUnit.SECONDS);
        assertTrue(completed, "Handler did not complete in time");
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
    }

    @Test
    void testPlayOnce() throws InterruptedException {
        CommunicationProtocol move = new CommunicationProtocol(new Cell(0, 0));
        System.out.println(move.getData());
        CommunicationProtocol exit = CommunicationProtocol.exit();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(
                        () -> {
                            handler.run();
                            latch.countDown();
                        })
                .start();
        pw.println(move);
        pw.println(exit);
        boolean completed = latch.await(2, TimeUnit.SECONDS);

        BoardPoint cell = game.getBoard().getPoint(new Cell(0, 0));
        assertTrue(completed, "Handler did not complete in time");
        assertEquals(BoardPoint.BLACK, cell);
    }

    @Test
    void testSendCommands() throws InterruptedException {
        CommunicationProtocol move = new CommunicationProtocol(new Cell(0, 0));
        CommunicationProtocol exit = CommunicationProtocol.exit();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(
                        () -> {
                            handler.run();
                            latch.countDown();
                        })
                .start();
        pw.println(move);
        pw.println(exit);
        boolean await = latch.await(2, TimeUnit.SECONDS);
        handler.sendCommands(CommunicationProtocol.pie());
        assertAll(
                () -> assertTrue(handler.isWaiting()),
                () -> assertTrue(await, "Handler did not complete in time"));
    }

    @BeforeEach
    void createHandler() throws IOException {
        FakeSocket fakeSocket = new FakeSocket();
        handler = new NetworkHandler(fakeSocket, game);
        pw = new PrintWriter(fakeSocket.getOutputStream(), true);
    }
}
