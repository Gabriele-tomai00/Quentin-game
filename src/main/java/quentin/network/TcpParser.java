package quentin.network;

import java.io.IOException;
import java.util.Scanner;

public class TcpParser {
  TCPServer server = new TCPServer(1234, "pwd");
  ServerInfo info = new ServerInfo("127.0.0.1", 1234, "server-username");
  TCPClient client = new TCPClient(info);

  public TcpParser() throws IOException {}

  public static void main(String[] args) throws IOException {
    TcpParser parser = new TcpParser();
    parser.run();
  }

  public void run() throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Command Parser started. Enter commands (type 'exit' to quit):");

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

  // Placeholder methods for each command
  private void startServer() {
    new Thread(
            () -> {
              server.start();
            })
        .start();
  }

  private void stopServer() throws IOException {
    System.out.println("Stopping server...");
    server.stop();
    System.out.println("Server is successfully closed.");
  }

  private void startClient() {
    new Thread(
            () -> {
              System.out.println("Starting client...");
              client.start();
              client.listenForMessages();
            })
        .start();
  }

  private void stopClient() throws IOException {
    System.out.println("Stopping client...");
    client.stop();
    System.out.println("Client is successfully closed.");
  }

  private void clientAuth() throws IOException {
    System.out.println("clientAuth command...");
    client.sendMessage("pwd"); // Password message
  }

  private void sendC() throws IOException {
    System.out.println("sendC command...");
    server.sendMessage("Hello Client!"); // Send message
  }

  private void sendS() throws IOException {
    System.out.println("sendS command...");
    client.sendMessage("Hello Server!"); // Send message
  }
}
