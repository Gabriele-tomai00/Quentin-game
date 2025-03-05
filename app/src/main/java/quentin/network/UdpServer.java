package quentin.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UdpServer {
  private static final int UDP_SERVER_PORT = 9876;
  private boolean discovery;
  private final String username;
  private final int tcpPort;
  private final String address;

  public UdpServer(String user, int tcpPortToComunicate) {
    username = user;
    tcpPort = tcpPortToComunicate;
    address = CorrectAddressGetter.getLocalIpAddress();
    discovery = true;
  }

  public void run() {
    while (discovery) {
    try (DatagramSocket socket = new DatagramSocket(UDP_SERVER_PORT)) {
//      System.out.println("UDP Server is listening: ip " + address + " port " + UDP_SERVER_PORT);
      socket.setSoTimeout(1000);
      byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        ServerInfo serverInfo = new ServerInfo(InetAddress.ofLiteral(address).toString(), tcpPort, username);
        buffer = serverInfo.toBytes();
        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
        socket.send(sendPacket);
    } catch (SocketTimeoutException e) {
      // do nothing
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  }

  public void stop() {
    discovery = false;
  }
}
