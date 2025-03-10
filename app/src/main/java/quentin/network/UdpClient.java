package quentin.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UdpClient {
  private final int serverPort;
  private String username;

  public UdpClient(int serverPort, String username) {
    this.serverPort = serverPort;
    this.username = username;
  }

  public NetworkInfo run() {
    while (!Thread.currentThread()
                  .isInterrupted()) {
      try (DatagramSocket clientSocket = new DatagramSocket()) {
        clientSocket.setBroadcast(true);
        String message = InetAddress.getLocalHost()
                                    .toString()
            + " - " + username;
        byte[] buffer = message.getBytes();
        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, serverPort);
        clientSocket.send(packet);
        buffer = new byte[1024];
        DatagramPacket recPacket = new DatagramPacket(buffer, buffer.length);
        clientSocket.setSoTimeout(1000);
        clientSocket.receive(recPacket);

        NetworkInfo info = NetworkInfo.fromString(new String(recPacket.getData(), 0, recPacket.getLength()));
        System.out.println("Server found: " + info);
        return info;
      } catch (SocketTimeoutException e) {
        // do nothing
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public void stop() {
    Thread.currentThread()
          .interrupt();
  }
}
