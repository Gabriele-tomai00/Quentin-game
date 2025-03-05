package quentin.network;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UdpClientTest {
  private static UdpClient client;
  private static Future<ServerInfo> submit;

  @BeforeAll
  static void startClient() {
    client = new UdpClient();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    submit = executor.submit(client::run);
  }

  @AfterAll
  static void stopClient() {
    client.stop();
  }

  @Test
  void testClient() throws IOException, InterruptedException, ExecutionException {
    try (DatagramSocket datagramSocket = new DatagramSocket(9876)) {
      byte[] buff = new byte[1024];
      DatagramPacket packet = new DatagramPacket(buff, buff.length);
      datagramSocket.receive(packet);
      ServerInfo info = new ServerInfo(InetAddress.getLocalHost()
                                                  .toString(),
          9876, "ca");
      byte [] sendBuff = info.toBytes();
      packet = new DatagramPacket(sendBuff, sendBuff.length, packet.getAddress(), packet.getPort());
      datagramSocket.send(packet);
//      could use a future to obtain data from client -> client = callable
      ServerInfo infoReceived = submit.get();
      assertAll(() -> assertEquals(info.address(), infoReceived.address()),
                () -> assertEquals(info.port(), infoReceived.port()),
                () -> assertEquals(info.username(), infoReceived.username()));
    }
  }

}
