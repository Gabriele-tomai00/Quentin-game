package quentin.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
  private static final int UDP_SERVER_PORT = 9876;
  public Boolean discovery = true;
  private Thread discoveryThread;
  private ServerInfo tcpServerInfo;

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

                    System.out.println("Broadcast request sent.");

                    DatagramPacket receivePacket =
                        new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    clientSocket.setSoTimeout(5000); // Timeout of 5 seconds
                    clientSocket.receive(receivePacket);

                    tcpServerInfo = ServerInfo.fromBytes(receivePacket.getData());
                    System.out.println("Received ServerInfo: " + tcpServerInfo);

                    break; // Exit loop once a response is received
                  } catch (Exception e) {
                    System.out.println("No response from server. Retrying...");
                  }

                  // Wait for x seconds before retrying
                  Thread.sleep(400);
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            });
    discoveryThread.start();
  }

  public void stopDiscovery() throws InterruptedException {
    discovery = false;
    discoveryThread.join();
    if (!discoveryThread.isAlive()) {
      System.out.println("C: client discovery stopped for sure");
    }
  }

  public static void main(String[] args) throws InterruptedException {
    UDPClient client = new UDPClient();
    client.startDiscovery(); // non-blocking
    Thread.sleep(4000);
    client.stopDiscovery(); // blocking
  }
}
