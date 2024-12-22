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
  private Boolean clientConnected = false;

  public TCPClient(ServerInfo info) {
    address = info.IpAddress();
    serverUsername = info.username();
    port = info.Port();
  }

  public void start() {
    try {
      socket = new Socket(address, port);
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println("TCP Client correctly connected");
      clientConnected = true;
    } catch (IOException e) {
      System.err.println("Error initializing the client connection: " + e.getMessage());
      stop();
    }
  }

  public void sendMessage(String message) {
    if (clientConnected) {
      System.out.println("message sent");
      out.println(message);
    } else System.out.println("message NOT sent");
  }

  public void listenForMessages() {
    if (clientConnected) {
      new Thread(
              () -> {
                try {
                  String serverMessage;
                  while ((serverMessage = in.readLine()) != null) {
                    System.out.println("Received from server: " + serverMessage);

                    if (!authenticated) {
                      if (serverMessage.equals("Password accepted from TCP server")) {
                        authenticated = true;
                      } else if (serverMessage.equals("server closed")) {
                        stop();
                      }
                    }
                    messageReceived = serverMessage;
                  }
                } catch (IOException e) {
                  // here when I call close() after a client connection
                }
              })
          .start();
    } else System.out.println("Can't receive from server because It's not connected");
  }

  public void stop() {
    out.println("client closed");
    try {
      if (in != null) in.close();
      if (out != null) out.close();
      if (socket != null) socket.close();
      System.out.println("Client process closed");
    } catch (IOException e) {
      //
    }
  }

  public String getServerUsername() {
    return serverUsername;
  }
}
