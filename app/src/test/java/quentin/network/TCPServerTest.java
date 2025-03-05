package quentin.network;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TCPServerTest {

    private static TcpServer tcpServer;
    private static Future<Socket> submit;

    @BeforeAll
    static void startServer() {
        tcpServer = new TcpServer(2000, "pass");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        submit = executor.submit(tcpServer::start);
    }

    @Test
    void testTcpServer() throws InterruptedException, ExecutionException, IOException {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 2000)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("1");
            writer.println("2");
            writer.println("pass");
            Socket serverSocket = submit.get();
            assertAll(
                    () -> assertTrue(serverSocket.isBound()),
                    () -> assertEquals(2000, serverSocket.getLocalPort()));
        }
    }
}
