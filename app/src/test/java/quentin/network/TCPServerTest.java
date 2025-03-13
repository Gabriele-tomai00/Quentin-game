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

class TCPServerTest {

    private TcpServer tcpServer;
    private Future<Socket> submit;
    private FakeSocket fakeSocket;

    @BeforeEach
    void startServer() throws IOException {
        fakeSocket = new FakeSocket();
        tcpServer =
                new TcpServer(2000, "pass") {
                    @Override
                    public Socket getConnection() throws IOException {
                        return fakeSocket;
                    }
                };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        submit = executor.submit(tcpServer::start);
    }

    @Test
    void testTcpServer() throws InterruptedException, ExecutionException, IOException {
        PrintWriter writer = new PrintWriter(fakeSocket.getOutputStream(), true);
        writer.println("1");
        writer.println("2");
        writer.println("pass");
        Socket serverSocket = submit.get();
        assertFalse(serverSocket.isClosed());
    }
}
