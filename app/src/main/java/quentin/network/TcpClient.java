package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TcpClient {
  private final int port;
  private final InetAddress address;
  private ClientAuthState authState = ClientAuthState.NOT_YET_AUTHENTICATED;
  private Scanner scanner;

  public TcpClient(ServerInfo info) throws UnknownHostException {
    address = InetAddress.getByName(info.address());
    port = info.port();
  }

  public Socket start() {
    scanner = new Scanner(System.in);

    try {
      Socket socket = new Socket(address, port);
      try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
          BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
        while (authState != ClientAuthState.AUTHENTICATED) {
          System.out.println("password > ");
          String password = scanner.nextLine()
                                   .trim();
          if ((password.equals("exit"))) { return null; }
          if ((password.length() != 5 || !password.matches("\\d{5}"))) {
            System.out.println("Invalid password, retry\n");
          }
          out.println(password);
          String message = in.readLine();
          if (message.equals("SERVER OK")) {
            authState = ClientAuthState.AUTHENTICATED;
          } else if (message.equals("SERVER ERR")) {
            authState = ClientAuthState.FAILED_AUTHENTICATION;
            throw new RuntimeException("Password was inputted incorrectly");
          }
        }
        return socket;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
