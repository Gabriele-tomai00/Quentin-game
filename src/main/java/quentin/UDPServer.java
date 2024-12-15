package quentin;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
  private static final int SERVER_PORT = 9876;

  public static void main(String[] args) {
    try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
      System.out.println("Server is listening on port " + SERVER_PORT);

      byte[] receiveBuffer = new byte[1024];
      byte[] sendBuffer;

      while (true) {
        // Blocking call: waits for an incoming packet from a client
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        serverSocket.receive(receivePacket);

        // Get client's address and port
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        System.out.println("Received request from client " + clientAddress + ":" + clientPort);

        // Prepare the response message
        String responseMessage =
            "Server IP: " + InetAddress.getLocalHost().getHostAddress() + ", Port: " + SERVER_PORT;
        sendBuffer = responseMessage.getBytes();

        // Send the response to the client
        DatagramPacket sendPacket =
            new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
        serverSocket.send(sendPacket);

        System.out.println("Response sent to client.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
