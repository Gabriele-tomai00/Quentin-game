package quentin.network;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UdpServerTest {
    private static FakeDatagramSocket socket;
    private static UdpServer server;
    private static final int PORT = 9876;
    private static final String SERVER_NAME = "test";

    @BeforeAll
    static void startServer() throws IOException {
        socket = new FakeDatagramSocket();
        server =
                new UdpServer(SERVER_NAME, PORT) {
                    @Override
                    protected DatagramSocket createSocket() throws SocketException {
                        return socket;
                    }
                };
    }

    @Test
    void testServer() throws IOException {
        NetworkInfo info =
                new NetworkInfo(InetAddress.getLoopbackAddress().getHostAddress(), "serverTest");
        byte[] buffer = info.getBytes();
        DatagramPacket packet =
                new DatagramPacket(buffer, buffer.length, InetAddress.ofLiteral("127.0.0.1"), PORT);
        socket.send(packet);

        server.call();

        buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength()).trim();
        assertTrue(received.contains(SERVER_NAME));
    }
}
