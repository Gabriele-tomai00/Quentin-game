package quentin.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UdpServerTest {

  @Test
  void testServer() throws IOException {
    UdpServer server = new UdpServer("default", 9876);
    new Thread(server::run).start();
    try (DatagramSocket datagramSocket = new DatagramSocket()) {
      byte[] buff = "mess".getBytes();
      DatagramPacket packet = new DatagramPacket(buff, buff.length, InetAddress.getLocalHost(), 9876);
      datagramSocket.send(packet);
      buff = new byte[1024];
      packet = new DatagramPacket(buff, buff.length);
      datagramSocket.receive(packet);
      Assertions.assertEquals(packet, "");
    }
    server.stop();
  }
}
