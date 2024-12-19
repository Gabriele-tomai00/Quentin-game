package quentin.network;

import java.io.*;
import java.net.*;

public class TCPClient implements TCPclientServerInterface {
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
  private String messageReceived;
  private Boolean authenticated = false;
  private final int port;
  private final String address;
  private final String serverUsername;
  private Boolean state;
  private Boolean running = false;

  // Constructor: connects to the server at the given IP and port
  public TCPClient(ServerInfo info) throws IOException {
    address = info.IpAddress();
    serverUsername = info.username();
    port = info.Port();
  }

  public String getServerUsername() {
    return serverUsername;
  }

  public void start() {
    try {
      socket = new Socket(address, port);
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (Exception e) {
      System.err.println("Unknown host: " + e.getMessage());
      state = false;
    }
    System.out.println("Client correctly connected");
    state = true;
    running = true;
  }

  // Sends a message to the server and waits for a response
  public void sendMessage(String message) throws IOException {
    if (state) out.println(message);
  }

  public void communicatePWD(String pwd) throws IOException {
    if (state) out.println(pwd);
  }

  // Receives messages from the server (runs in a separate thread)
  public void listenForMessages() {
    if (state) {
      new Thread(
              () -> {
                try {
                  String serverMessage;
                  while (running && (serverMessage = in.readLine()) != null) {
                    System.out.println("Received from server: " + serverMessage);
                    if (serverMessage.equals("Password accepted")) {
                      authenticated = true;
                      messageReceived = serverMessage;
                    } else if (serverMessage.equals("server closed")) {
                      stop();
                    }
                  }
                } catch (IOException e) {
                  // here when I call close() after a client connection
                }
              })
          .start();
    }
  }

  // Closes all resources used by the client
  public void stop() {
    out.println("client closed");
    running = false;
    try {
      in.close();
      out.close();
      socket.close();
      System.out.println("Client process closed");
    } catch (IOException e) {
      //
    }
  }

  public static void main(String[] args) throws InterruptedException {
    try {
      ServerInfo info = new ServerInfo("127.0.0.1", 1234, "server-username");
      TCPClient client = new TCPClient(info);
      client.start();
      if (client.running) {
        client.listenForMessages();

        // Send the first message with password for authentication
        client.communicatePWD("firstP"); // Password message
        Thread.sleep(1000);

        client.communicatePWD("secretPassword"); // Password message
        Thread.sleep(1000);
        String responseAfterCode = client.messageReceived;

        Thread.sleep(3000);
        client.stop();

        // Only continue if the server accepted the password
        if ("Password accepted".equals(responseAfterCode)) {
          for (int i = 1; i <= 100; i++) {
            client.sendMessage(i + " Hello Server!"); // Send message
            Thread.sleep(1000);
          }
        } else {
          System.out.println("Invalid password, closing connection.");
        }

        //      client.close(); // Close connection
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
