package quentin.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

public class UdpServer implements Callable<Void> {
  private final String username;
  private final int port;

  public UdpServer(String username, int port) {
    this.username = username;
    this.port = port;
  }

  public Void call() throws IOException {
    System.out.println("UDP Server is listening on port: " + port);
    while (!Thread.currentThread()
                  .isInterrupted()) {
      try (DatagramSocket socket = createSocket()) {
        socket.setSoTimeout(1000);
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Connection from: " + message);
        NetworkInfo serverInfo = new NetworkInfo(InetAddress.getLocalHost()
                                                            .getHostAddress(),
                                                 username);
        System.out.println(serverInfo.address());
        buffer = serverInfo.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
        socket.send(sendPacket);

        return null;
      } catch (SocketTimeoutException e) {
        // do nothing
      }
    }
    return null;
  }

  protected DatagramSocket createSocket() throws SocketException {
    return new DatagramSocket(port);
  }
}
