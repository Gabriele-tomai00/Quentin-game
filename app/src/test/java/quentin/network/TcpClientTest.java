package quentin.network;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quentin.StreamUtility;

class TcpClientTest {

    private static final int PORT = 3000;
    private FakeSocket clientSocket;

    @BeforeEach
    void setOutput() throws IOException {
        PipedInputStream clientIn = new PipedInputStream();
        PipedInputStream serverIn = new PipedInputStream();
        PipedOutputStream clientOut = new PipedOutputStream(serverIn);
        PipedOutputStream serverOut = new PipedOutputStream(clientIn);
        clientSocket = new FakeSocket(clientIn, clientOut);
        FakeSocket serverSocket = new FakeSocket(serverIn, serverOut);

        TcpServer server =
                new TcpServer(PORT, "33333") {
                    @Override
                    public Socket getConnection() throws IOException {
                        return serverSocket;
                    }
                };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(server);
    }

    @Test
    void testTcpClient() throws IOException {
        StreamUtility.provideInput("11111\n33333\n");
        TcpClient client = new TcpClient(clientSocket);
        Socket returnedSocket = client.call();
        assertFalse(returnedSocket.isClosed());
    }
}
