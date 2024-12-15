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
    if ((message = in.readLine()) != null) {
      System.out.println("Received first message: " + message);
      // out.println("Echo: " + message);  // Respond back with the received message
    }
  }

  // Receives messages from the client (runs in a separate thread)
  public void listenForMessages() {
    new Thread(
            () -> {
              try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                  System.out.println("Received from client: " + serverMessage);
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  // Sends a message to the client and waits for a response
  public void communicate(String message) throws IOException {
    out.println(message);
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
      server.start(); // blocking
      server.listenForMessages();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
