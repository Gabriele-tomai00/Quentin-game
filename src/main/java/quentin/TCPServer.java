package quentin;

import java.io.*;
import java.net.*;

public class TCPServer {
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;
  private static final String PASSWORD = "secretPassword"; // The correct password
  public Boolean isClientAuth = false;
  public int port;
  public String recivedMessage;

  // Constructor: initializes the server socket on the given port
  public TCPServer(int port1) throws IOException {
    port = port1;
  }

  // Starts the server to accept client connections and handle communication
  public void startForAuth() throws IOException {
    serverSocket = new ServerSocket(port);
    System.out.println("Server started, waiting for client...");
    try {
      clientSocket = serverSocket.accept(); // Blocking call: waits for a client to connect
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      System.out.println("Client connected: " + clientSocket.getInetAddress());
      String message;
      for (int attempt = 0; attempt < 3; attempt++) {
        if ((message = in.readLine()) != null) {
          System.out.println("Received first message: " + message);
          if (!message.equals(PASSWORD)) {
            System.out.println("Invalid password, retry");
            out.println("Invalid password");
          } else {
            isClientAuth = true;
            out.println("Password accepted");
            listenForMessages();
            return;
          }
        }
      }
      System.out.println("too many attempts, exiting");
      out.println("too many attempts, exiting");
      close(); // Close connection if password is incorrect

    } catch (SocketException e) {
      // when close() is called, the function goes here
      System.out.println("Server socket closed, stopping server...");
    }
  }

  // Receives messages from the client (runs in a separate thread)
  public void listenForMessages() {
    new Thread(
            () -> {
              try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                  if (isClientAuth) {
                    System.out.println("Received from client: " + serverMessage);
                    recivedMessage = serverMessage;
                  } else System.out.println("Can't receive from client because It's not auth");
                }
              } catch (IOException e) {
                // here when I call close() after a client connection
              }
            })
        .start();
  }

  // Sends a message to the client and waits for a response
  public void communicate(String message) throws IOException {
    // send only if client il authenticated
    if (isClientAuth) {
      out.println(message);
    }
  }

  // Closes all resources used by the server
  public void close() {
    System.out.println("S: inizio");
    try {
      if (clientSocket != null) {
        clientSocket.close();
      }
      if (serverSocket != null) {
        serverSocket.close();
      }
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
    } catch (IOException e) {
      //
    }
    System.out.println("Server process closed");
  }

  public static void main(String[] args) throws InterruptedException {
    try {
      TCPServer server = new TCPServer(1234);
      server.startForAuth(); // blocking
      server.listenForMessages();
      Thread.sleep(1000);

      for (int i = 1; i <= 100; i++) {
        server.communicate(i + " Hello Client!"); // Send message
        Thread.sleep(1000);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
