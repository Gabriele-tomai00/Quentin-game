package quentin;

import java.io.*;
import java.net.*;

public class TCPServer {
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;
  private static final String PASSWORD = "secretPassword"; // The correct password

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
    for (int attempt = 0; attempt < 3; attempt++) { // three attempts
      if ((message = in.readLine()) != null) {
        System.out.println("Received first message: " + message);
        // Check if the first message contains the correct password
        if (!message.equals(PASSWORD)) {
          System.out.println("Invalid password, retry");
          out.println("Invalid password");
        } else {
          out.println("Password accepted");
          return;
        }
      }
    }
    System.out.println("too many attempts, exiting");
    out.println("too many attempts, exiting");
    close(); // Close connection if password is incorrect
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
    if (in != null) in.close();
    if (out != null) out.close();
    if (clientSocket != null) clientSocket.close();
    if (serverSocket != null) serverSocket.close();
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
