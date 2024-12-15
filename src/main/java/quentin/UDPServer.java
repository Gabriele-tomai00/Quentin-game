package quentin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPServer {
  private static final int UDP_SERVER_PORT = 9876;
  private volatile boolean discovery = true; // Use volatile for thread-safe visibility
  private Thread discoveryThread;

  public void startServer() {
    discoveryThread =
        new Thread(
            () -> {
              try (DatagramSocket serverSocket = new DatagramSocket(UDP_SERVER_PORT)) {
                System.out.println("Server is listening on port " + UDP_SERVER_PORT);

                byte[] receiveBuffer = new byte[1024];
                byte[] sendBuffer;

                serverSocket.setSoTimeout(1000); // Set timeout of 1 second

                while (discovery) {
                  try {
                    // Blocking call: waits for an incoming packet from a client
                    DatagramPacket receivePacket =
                        new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    serverSocket.receive(receivePacket);

                    // Get client's address and port
                    InetAddress clientAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();

                    System.out.println(
                        "Received request from client " + clientAddress + ":" + clientPort);

                    // Prepare the response message
                    String responseMessage =
                        "Server IP: "
                            + InetAddress.getLocalHost().getHostAddress()
                            + ", Port: "
                            + UDP_SERVER_PORT;
                    sendBuffer = responseMessage.getBytes();

                    // Send the response to the client
                    DatagramPacket sendPacket =
                        new DatagramPacket(
                            sendBuffer, sendBuffer.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);

                    System.out.println("Response sent to client.");
                  } catch (SocketTimeoutException e) {
                    // Timeout occurred: Check if we need to stop
                    if (!discovery) {
                      System.out.println("Timeout occurred, stopping server.");
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              } catch (Exception e) {
                e.printStackTrace();
              } finally {
                System.out.println("Server has stopped.");
              }
            });
    discoveryThread.start();
  }

  public void stopServer() throws InterruptedException {
    discovery = false;
    discoveryThread.join(); // Wait for the thread to terminate
    if (!discoveryThread.isAlive()) {
      System.out.println("S: Server stopped successfully.");
    }
  }

  public static void main(String[] args) throws InterruptedException {
    UDPServer server = new UDPServer();
    server.startServer(); // non-blocking
    Thread.sleep(1000);
    server.stopServer(); // blocking
  }
}
