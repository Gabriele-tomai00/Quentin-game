package quentin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
  private static final int SERVER_PORT = 9876;
  private static final String BROADCAST_IP = "255.255.255.255"; // Broadcast address

  public static void main(String[] args) {
    try (DatagramSocket clientSocket = new DatagramSocket()) {
      System.out.println("Client started.");

      byte[] sendBuffer = "Requesting server information".getBytes();
      byte[] receiveBuffer = new byte[1024];

      clientSocket.setBroadcast(true); // Enable broadcast mode

      while (true) {
        try {
          // Send a broadcast request
          InetAddress broadcastAddress = InetAddress.getByName(BROADCAST_IP);
          DatagramPacket sendPacket =
              new DatagramPacket(sendBuffer, sendBuffer.length, broadcastAddress, SERVER_PORT);
          clientSocket.send(sendPacket);

          System.out.println("Broadcast request sent.");

          // Blocking call: waits for a response from the server
          DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
          clientSocket.setSoTimeout(5000); // Timeout of 5 seconds
          clientSocket.receive(receivePacket);

          String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
          System.out.println("Response received: " + serverResponse);

          break; // Exit loop once a response is received
        } catch (Exception e) {
          System.out.println("No response from server. Retrying...");
        }

        // Wait for 10 seconds before retrying
        Thread.sleep(5000);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
