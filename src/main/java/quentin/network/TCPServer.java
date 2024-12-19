package quentin.network;

import java.io.*;
import java.net.*;

public class TCPServer implements TCPclientServerInterface {
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;
  private final String PASSWORD;
  public Boolean isClientAuth = false;
  public int port;
  public String messageReceived;
  private Thread waitMessageThread;

  // Constructor: initializes the server socket on the given port
  public TCPServer(int port1, String pwd) throws IOException {
    port = port1;
    PASSWORD = pwd;
  }

  // Starts the server to accept client connections and handle communication
  public void start() {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.err.println("Error during socket creation: ");
    }
    System.out.println("TCP Server started on port " + port + ", waiting for client...");
    try {
      clientSocket = serverSocket.accept(); // Blocking call: waits for a client to connect
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      System.out.println("Client connected: " + clientSocket.getInetAddress());
      System.out.println("CODE: " + PASSWORD);
      String message;
      for (int attempt = 0; attempt < 3; attempt++) {
        if ((message = in.readLine()) != null) {
          System.out.println("Received first message: " + message);
          if (!message.equals(PASSWORD)) {
            System.out.println("Invalid password, retry");
            out.println("Invalid password");
          } else {
            System.out.println("Password accepted");
            isClientAuth = true;
            out.println("Password accepted");
            listenForMessages();
            return;
          }
        }
      }
      // can send and receive
      System.out.println("too many attempts, exiting");
      out.println("too many attempts, exiting");
      stop(); // Close connection if password is incorrect

    } catch (SocketException e) {
      // when close() is called, the function goes here
      System.out.println("Server socket closed, stopping server...");
    } catch (IOException e) {
      System.err.println("IOException: problems with client connection");
    }
  }

  // Receives messages from the client (runs in a separate thread)
  public void listenForMessages() {
    // here when I call close() after a client connection
    waitMessageThread =
        new Thread(
            () -> {
              try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                  if (isClientAuth) {
                    System.out.println("Received from client: " + serverMessage);
                    messageReceived = serverMessage;
                  } else System.out.println("Can't receive from client because It's not auth");
                }
              } catch (IOException e) {
                // here when I call close() after a client connection
              }
            });
    waitMessageThread.start();
  }

  public void sendMessage(String message) throws IOException {
    // send only if client is authenticated
    if (isClientAuth) {
      out.println(message);
    }
  }

  // Closes all resources used by the server
  public void stop() {
    out.println("server closed");
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
    try {
      waitMessageThread.join(); // Wait for the thread to terminate
    } catch (InterruptedException e) {
    }
    System.out.println("Server process closed");
  }

  public static void main(String[] args) throws InterruptedException {
    try {
      TCPServer server = new TCPServer(1234, "pwd");
      server.start(); // blocking
      server.listenForMessages();
      Thread.sleep(1000);

      for (int i = 1; i <= 100; i++) {
        server.sendMessage(i + " Hello Client!"); // Send message
        Thread.sleep(1000);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
