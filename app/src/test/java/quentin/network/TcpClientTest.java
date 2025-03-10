package quentin.network;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.net.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quentin.StreamUtility;

class TcpClientTest {

    private static final int PORT = 3000;
    FakeSocket socket;

    @BeforeEach
    void setOutput() throws IOException {
        socket = new FakeSocket();

        TcpServer server =
                new TcpServer(PORT, "33333") {
                    @Override
                    public Socket getConnection() throws IOException {
                        return socket;
                    }
                };
        new Thread(server::start).start();
    }

    @Test
    void testTcpClient() {
        StreamUtility.provideInput("11111\n33333\n");
        TcpClient client = new TcpClient(socket);
        Socket clientSocket = client.start();
        assertFalse(clientSocket.isClosed());
    }
}
