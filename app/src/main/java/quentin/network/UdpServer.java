package quentin.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UdpServer {
  private boolean discovery;
  private final String username;
  private final int port;
  private final String address;

  public UdpServer(String user, int port) {
    username = user;
    this.port = port;
    address = CorrectAddressGetter.getLocalIpAddress();
    discovery = true;
  }

  public void run() {
    while (discovery) {
      try (DatagramSocket socket = new DatagramSocket(port)) {
        System.out.println("UDP Server is listening: ip " + address + " port " + port);
        socket.setSoTimeout(1000);
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Connection from: " + message);
        NetworkInfo serverInfo = new NetworkInfo(InetAddress.ofLiteral(address), username);
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
