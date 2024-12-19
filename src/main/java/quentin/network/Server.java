package quentin.network;

import java.io.IOException;
import java.util.Random;
import quentin.SettingHandler;

public class Server {

  private SettingHandler settingHandler;
  private UDPServer udpServer;
  private TCPServer tcpServer;
  public String codeForClientAuth;
  private String username;
  private int tcpPort;

  public Server() {
    generateRandomCode();
    setNetworkInfo();
  }

  public void setNetworkInfo() {
    settingHandler = new SettingHandler();
    username = settingHandler.getUsername();
    tcpPort = settingHandler.getPort();
  }

  public void start() throws IOException {
    udpServer = new UDPServer(username, tcpPort);
    udpServer.startServer();
    tcpServer = new TCPServer(tcpPort, codeForClientAuth);
    tcpServer.start();
  }

  public void stop() throws InterruptedException {
    udpServer.stopServer();
    tcpServer.stop();
  }

  public void generateRandomCode() {
    long seed = System.currentTimeMillis();
    Random random = new Random(seed);
    StringBuilder randomNumbers = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      int randomNumber = random.nextInt(10);
      randomNumbers.append(randomNumber);
    }
    codeForClientAuth = randomNumbers.toString();
  }

  public void sendMessage(String message) throws IOException {
    tcpServer.sendMessage(message);
  }

  public String getCodeForClientAuth() {
    return codeForClientAuth;
  }
}
