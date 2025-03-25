package quentin.network;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UdpClientTest {
    private static final int PORT = 2000;
    private static UdpClient client;
    private static final String SERVER_NAME = "testServer";

    @BeforeAll
    static void startClient() throws IOException {
        PipedInputStream clientIn = new PipedInputStream();
        PipedInputStream serverIn = new PipedInputStream();
        PipedOutputStream clientOut = new PipedOutputStream(serverIn);
        PipedOutputStream serverOut = new PipedOutputStream(clientIn);
        FakeDatagramSocket serverSocket = new FakeDatagramSocket(clientIn, clientOut);
        FakeDatagramSocket clientSocket = new FakeDatagramSocket(serverIn, serverOut);
        UdpServer server =
                new UdpServer(SERVER_NAME, PORT) {
                    @Override
                    protected DatagramSocket createSocket() throws SocketException {
                        return serverSocket;
                    }
                };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(server);
        client =
                new UdpClient("testClient", PORT) {
                    @Override
                    protected DatagramSocket createSocket() throws SocketException {
                        return clientSocket;
                    }
                };
    }

    @Test
    void testClient() throws IOException {
        NetworkInfo infoReceived = client.call();
        assertTrue(infoReceived.username().contains(SERVER_NAME));
    }
}
