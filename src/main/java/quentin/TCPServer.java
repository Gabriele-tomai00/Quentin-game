package quentin;

import java.io.*;
import java.net.*;

public class TCPServer {
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  // Constructor: initializes the server socket on the given port
  public TCPServer(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    System.out.println("Server started, waiting for client...");
  }

  // Starts the server to accept client connections and handle communication
  public void start() throws IOException {
    clientSocket = serverSocket.accept(); // Blocking call: waits for a client to connect
    out = new PrintWriter(clientSocket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    System.out.println("Client connected: " + clientSocket.getInetAddress());

    String message;
    while ((message = in.readLine()) != null) {
      System.out.println("Received: " + message);
      out.println("Echo: " + message); // Respond back with the received message
    }
  }

  // Closes all resources used by the server
  public void close() throws IOException {
    in.close();
    out.close();
    clientSocket.close();
    serverSocket.close();
  }

  public static void main(String[] args) {
    try {
      TCPServer server = new TCPServer(1234);
      server.start(); // Start server
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
