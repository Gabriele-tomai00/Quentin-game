package quentin.network;

import java.net.*;

public class UDPClient {
  private static final int UDP_SERVER_PORT = 9876;
  public Boolean discovery = true;
  private Thread discoveryThread;
  private ServerInfo tcpServerInfo;
  private Runnable onDiscoveredCallback;

  public ServerInfo getTcpServerInfo() {
    return tcpServerInfo;
  }

  public void startDiscovery() {
    discoveryThread =
        new Thread(
            () -> {
              try (DatagramSocket clientSocket = new DatagramSocket()) {
                System.out.println("C: server discovery started");
                byte[] sendBuffer = "Requesting server information".getBytes();
                byte[] receiveBuffer = new byte[1024];

                clientSocket.setBroadcast(true); // Enable broadcast mode

                while (discovery) {
                  try {
                    // Send a broadcast request
                    InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                    DatagramPacket sendPacket =
                        new DatagramPacket(
                            sendBuffer, sendBuffer.length, broadcastAddress, UDP_SERVER_PORT);
                    clientSocket.send(sendPacket);

                    System.out.println("Broadcast request sent");

                    DatagramPacket receivePacket =
                        new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    clientSocket.setSoTimeout(5000);
                    clientSocket.receive(receivePacket);

                    tcpServerInfo = ServerInfo.fromBytes(receivePacket.getData());
                    System.out.println("Received ServerInfo: " + tcpServerInfo);
                    if (onDiscoveredCallback != null) onDiscoveredCallback.run();

                    break;
                  } catch (Exception e) {
                    // here if server is not being found yet
                  }

                  Thread.sleep(400);
                }
              } catch (Exception e) {
                System.err.println("Unexpected error in udp client discovery");
              }
            });
    discoveryThread.start();
  }

  public void stopDiscovery() {
    discovery = false;
    try {
      discoveryThread.join();
      if (!discoveryThread.isAlive()) {
        System.out.println("client discovery correctly stopped");
      }
    } catch (InterruptedException e) {
      System.err.println("Error stopping udp client discovery");
    }
  }

  public void setOnDiscoveredCallback(Runnable callback) {
    this.onDiscoveredCallback = callback;
  }
}
