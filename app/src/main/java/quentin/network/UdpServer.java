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
    try (DatagramSocket socket = new DatagramSocket()) {
      System.out.println("UDP Server is listening: ip " + address + " port " + UDP_SERVER_PORT);
//      socket.setSoTimeout(1000);    
      byte[] receiveBuffer = new byte[1024];
      while (discovery) {
        DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(packet);
        ServerInfo serverInfo = new ServerInfo(InetAddress.ofLiteral(address), tcpPort, username);
        byte[] sendBuffer = serverInfo.toBytes();
        packet = new DatagramPacket(sendBuffer, sendBuffer.length, packet.getAddress(), packet.getPort());
        socket.send(packet);
      }
    } catch (SocketTimeoutException e) {
      // do nothing
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    discovery = false;
  }
}
