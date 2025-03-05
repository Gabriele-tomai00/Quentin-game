package quentin.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

public class UdpClient {
  private static final int UDP_SERVER_PORT = 9876;
  private boolean discovery = true;
  private final String clientAddress;

  public UdpClient() {
    clientAddress = CorrectAddressGetter.getLocalIpAddress();
  }

  public ServerInfo run() {
    while (discovery) {
//    System.out.println("address of udp client: " + clientAddress);
      try (DatagramSocket clientSocket = new DatagramSocket(
          new InetSocketAddress(InetAddress.getByName(clientAddress), 0))) {
        clientSocket.setBroadcast(true);

        byte[] buffer = "GET INFO".getBytes();
        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, UDP_SERVER_PORT);
        clientSocket.send(packet);

//        System.out.println("Broadcast request sent (to: " + broadcastAddress + " port: " + UDP_SERVER_PORT + ")");

        buffer = new byte[1024];
        DatagramPacket recPacket = new DatagramPacket(buffer, buffer.length);
        clientSocket.setSoTimeout(1000);
        clientSocket.receive(recPacket);
        String info = new String(recPacket.getData(), 0, recPacket.getLength());
        return ServerInfo.fromString(info);
      } catch (SocketTimeoutException e) {
        // do nothing
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public void stop() {
    discovery = false;
  }
}
