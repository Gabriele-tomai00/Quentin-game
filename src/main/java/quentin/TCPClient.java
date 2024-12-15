package quentin;

import java.io.*;
import java.net.*;

public class TCPClient {
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  // Constructor: connects to the server at the given IP and port
  public TCPClient(String ip, int port) throws IOException {
    socket = new Socket(ip, port);
    out = new PrintWriter(socket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  // Sends a message to the server and waits for a response
  public void communicate(String message) throws IOException {
    out.println(message);
  }

  // Receives messages from the server (runs in a separate thread)
  public void listenForMessages() {
    new Thread(
            () -> {
              try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                  System.out.println("Received from server: " + serverMessage);
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  // Closes all resources used by the client
  public void close() throws IOException {
    in.close();
    out.close();
    socket.close();
  }

  public static void main(String[] args) throws InterruptedException {
    try {
      TCPClient client = new TCPClient("127.0.0.1", 1234);
      client.listenForMessages();

      for (int i = 1; i <= 100; i++) {
        client.communicate(i + " Hello Server!"); // Send message
        Thread.sleep(1000);
      }
      client.close(); // Close connection

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
