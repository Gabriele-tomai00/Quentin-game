package quentin.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UdpServerTest {
    private static UdpServer server;

    @BeforeAll
    static void startServer() {
        server = new UdpServer("default", 9876);
        new Thread(server::run).start();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void testServer() throws IOException {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            byte[] buff = "mess".getBytes();
            DatagramPacket packet =
                    new DatagramPacket(buff, buff.length, InetAddress.getLocalHost(), 9876);
            datagramSocket.send(packet);
            buff = new byte[1024];
            packet = new DatagramPacket(buff, buff.length);
            datagramSocket.receive(packet);
            String data = new String(packet.getData(), 0, packet.getLength());
            assertEquals("10.110.40.198 - default", data);
        }
    }
}
