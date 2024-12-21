package quentin.network;

import java.io.IOException;
import java.util.Scanner;

public class TcpUdpParser {
  Client client = new Client();
  Server server = new Server();

  public static void main(String[] args) {
    TcpUdpParser parser = new TcpUdpParser();
    parser.run();
  }

  public void run() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter commands (type 'exit' to quit):");

    while (true) {
      System.out.print("> ");
      String command = scanner.nextLine().trim().toLowerCase();

      if (command.equals("exit")) {
        System.out.println("Exiting Command Parser.");
        break;
      }

      switch (command) {
        case "startserver", "starts":
          startServer();
          break;
        case "stopserver", "stops":
          stopServer();
          break;
        case "startclient", "startc":
          startClient();
          break;
        case "stopclient", "stopc":
          stopClient();
          break;
        case "sendtoc":
          sendC();
          break;
        case "sendtos":
          sendS();
          break;
        case "clienta":
          clientAuth();
          break;
        case "":
          break;
        default:
          System.out.println("Unknown command: " + command);
          break;
      }
    }

    scanner.close();
  }

  private void startServer() {
    new Thread(
            () -> {
              System.out.println("Starting server...");
              server.start();
            })
        .start();
  }

  private void stopServer() {
    System.out.println("Stopping server...");
    server.stop();
  }

  private void startClient() {
    new Thread(
            () -> {
              System.out.println("Starting client...");
              try {
                client.startDiscovery();
              } catch (IOException e) {
                System.err.println("Error initializing the client connection");
              }
            })
        .start();
    System.out.println("Once recived server info, type 'starttcpclient' to link with server");
  }

  private void stopClient() {
    client.stopDiscovery();
  }

  private void clientAuth() {
    System.out.println("clientAuth command...");
    Scanner scanner = new Scanner(System.in);
    String password;
    System.out.print("Enter a 5-digit numeric password: ");
    password = scanner.nextLine().trim();
    client.trySendAuthentication(password);
  }

  private void sendC() {
    System.out.println("sendC command...");
    server.sendMessage("Hello Client!"); // Send message
  }

  private void sendS() {
    System.out.println("sendS command...");
    client.sendMessage("Hello Server!"); // Send message
  }
}
