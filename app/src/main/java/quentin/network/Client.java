package quentin.network;

import quentin.SettingHandler;

public class Client {

  private final UDPClient client;
  private final String username;

  public Client() {
    client = new UDPClient();
    SettingHandler settingHandler = new SettingHandler();
    username = settingHandler.getUsername();
  }

  public void startDiscovery() {
    TCPClient tcpClient = new TCPClient(client.call());
    tcpClient.start();
  }

  public void stop() {
    client.stopDiscovery();
  }
}
