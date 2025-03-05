package quentin.network;

import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Callable;

import quentin.SettingHandler;

public class Server implements Callable<Socket> {

  private final String codeForClientAuth;
  private final String username;
  private final int tcpPort;

  public Server() {
    codeForClientAuth = generateRandomCode(System.currentTimeMillis());
    SettingHandler settingHandler = new SettingHandler();
    username = settingHandler.getUsername();
    tcpPort = settingHandler.getPort();
  }

  @Override
  public Socket call() {
    UdpServer udpServer = new UdpServer(username, tcpPort);
    udpServer.run();
    TcpServer tcpServer = new TcpServer(tcpPort, codeForClientAuth);
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
