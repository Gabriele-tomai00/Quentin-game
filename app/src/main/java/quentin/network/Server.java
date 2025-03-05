package quentin.network;

import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Callable;

import quentin.SettingHandler;

public class Server implements Callable<Socket> {

  private final SettingHandler handler;

  public Server(SettingHandler handler) {
    this.handler = handler;
  }

  @Override
  public Socket call() {
    UdpServer udpServer = new UdpServer(handler.getUsername(), handler.getPort());
    udpServer.run();
    TcpServer tcpServer = new TcpServer(handler.getPort(), generateRandomCode(System.currentTimeMillis()));
    return tcpServer.start();
  }

  public static String generateRandomCode(long seed) {
    Random random = new Random(seed);
    StringBuilder randomNumbers = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      int randomNumber = random.nextInt(10);
      randomNumbers.append(randomNumber);
    }
    return randomNumbers.toString();
  }
}
