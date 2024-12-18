package quentin;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class TCPClient {
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
  public String messageRecived;
  public Boolean authenticated = false;
  public int port;
  public String address;
  public String state;
  public Boolean running = false;

  // Constructor: connects to the server at the given IP and port
  public TCPClient(String ip, int p) throws IOException {
    address = ip;
    port = p;
  }

  public void start() {
    try {
      socket = new Socket(address, port);
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (Exception e) {
      System.err.println("Unknown host: " + e.getMessage());
      state = "connection failed";
    }
    System.out.println("Client correctly connected");
    state = "connected";
    running = true;
  }

  // Sends a message to the server and waits for a response
  public void communicate(String message) throws IOException {
    if (Objects.equals(state, "connected")) out.println(message);
  }

  public void communicatePWD(String pwd) throws IOException {
    if (Objects.equals(state, "connected")) out.println(pwd);
  }

  // Receives messages from the server (runs in a separate thread)
  public void listenForMessages() {
    System.out.println("Dentro listenForMessages, state: " + state + " running: " + running);
    if (Objects.equals(state, "connected")) {
      new Thread(
              () -> {
                try {
                  String serverMessage;
                  while (running && (serverMessage = in.readLine()) != null) {
                    System.out.println("Received from server: " + serverMessage);
                    if (serverMessage.equals("Password accepted")) {
                      authenticated = true;
                      messageRecived = serverMessage;
                    }
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              })
          .start();
    }
  }

  // Closes all resources used by the client
  public void close() throws IOException {
    running = false;
    in.close();
    out.close();
    socket.close();
    System.out.println("Client process closed");
  }

  public static void main(String[] args) throws InterruptedException {
    try {
      TCPClient client = new TCPClient("127.0.0.1", 1234);
      client.start();
      if (client.state.equals("connected")) {
        System.out.println(client.state);
        client.listenForMessages();

        // Send the first message with password for authentication
        client.communicatePWD("firstP"); // Password message
        Thread.sleep(1000);

        client.communicatePWD("secretPassword"); // Password message
        Thread.sleep(1000);
        String responseAfterCode = client.messageRecived;

        Thread.sleep(3000);
        client.close();

        // Only continue if the server accepted the password
        if ("Password accepted".equals(responseAfterCode)) {
          for (int i = 1; i <= 100; i++) {
            client.communicate(i + " Hello Server!"); // Send message
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
