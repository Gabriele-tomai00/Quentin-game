package quentin.network;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TcpClientTest {

  @BeforeAll
  static void setOutput() {
    TcpServer server = new TcpServer(3000, "33333");
    new Thread(server::start).start();
  }

  void provideInput(String data) {
    ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes());
    System.setIn(is);
  }

  @Test
  void testTcpClient() throws UnknownHostException {
    provideInput("11111\n33333\n");
    TcpClient client = new TcpClient(new ServerInfo("192.168.1.197", 3000, "aleq"));
    Socket socket = client.start();
    assertTrue(socket.isBound());
  }

}
