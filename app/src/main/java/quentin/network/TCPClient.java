package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
  private final int port;
  private final String address;
  private ClientAuthState authState = ClientAuthState.NOT_YET_AUTHENTICATED;

  public TCPClient(ServerInfo info) {
    address = info.IpAddress();
    port = info.Port();
  }

  public Socket start() {
    try (Socket socket = new Socket(address, port)) {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//      System.out.println("TCP Client correctly authenticated, now you must send the password");
//      TODO add prompt for inserting password
      while (authState != ClientAuthState.AUTHENTICATED) {
        String serverMessage = in.readLine();
        if (serverMessage.equals("Password accepted from TCP server")) {
          authState = ClientAuthState.AUTHENTICATED;
        } else if (serverMessage.equals("server closed")) {
          authState = ClientAuthState.FAILED_AUTHENTICATION;
          throw new RuntimeException();
        }
      }
      return socket;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
