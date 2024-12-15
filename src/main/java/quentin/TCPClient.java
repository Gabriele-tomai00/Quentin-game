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
    String response = in.readLine(); // Blocking call: waits for the server's response
    System.out.println("Server response: " + response);
  }

  // Closes all resources used by the client
  public void close() throws IOException {
    in.close();
    out.close();
    socket.close();
  }

  public static void main(String[] args) {
    try {
      TCPClient client = new TCPClient("127.0.0.1", 1234);
      client.communicate("Hello Server!"); // Send message
      client.close(); // Close connection
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
