package quentin.network;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TcpServerTest {

    private Future<Socket> submit;
    private FakeSocket fakeSocket;
    private static final String RIGHT_PASS = "pass";

    @Test
    void testTcpServer() throws InterruptedException, ExecutionException {
        PrintWriter writer = new PrintWriter(fakeSocket.getOutputStream(), true);
        writer.println("1");
        writer.println("2");
        writer.println(RIGHT_PASS);
        Socket serverSocket = submit.get();
        assertFalse(serverSocket.isClosed());
    }

    @BeforeEach
    void startServer() throws IOException {
        fakeSocket = new FakeSocket();
        TcpServer tcpServer =
                new TcpServer(2000, RIGHT_PASS) {
                    @Override
                    public Socket getConnection() {
                        return fakeSocket;
                    }
                };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        submit = executor.submit(tcpServer);
    }
}
