package quentin.network;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import quentin.SettingHandler;

public class Client implements Callable<Socket> {

  private final SettingHandler handler;

  public Client() {
    handler = new SettingHandler();
  }

  public Socket call() throws UnknownHostException {
    UdpClient udpClient = new UdpClient(handler.getPort(), handler.getUsername());
    NetworkInfo info = udpClient.run();
    TcpClient tcpClient = new TcpClient(handler.getPort(), info.address());
    return tcpClient.start();
  }
}
