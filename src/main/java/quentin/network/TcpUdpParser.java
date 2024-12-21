package quentin.network;

import java.io.IOException;
import java.util.Scanner;

public class TcpUdpParser {
  Client client = new Client();
  Server server = new Server();

  public static void main(String[] args) throws IOException, InterruptedException {
    TcpUdpParser parser = new TcpUdpParser();
    parser.run();
  }

  public void run() throws IOException, InterruptedException {
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
        case "starttcpclient", "starttcpc":
          startTCPclient();
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
              try {
                server.start();
              } catch (IOException e) {
                // throw new RuntimeException(e);
              }
            })
        .start();
    forceWrap();
  }

  private void stopServer() throws InterruptedException {
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
                // throw new RuntimeException(e);
              }
            })
        .start();
  }

  private void stopClient() throws InterruptedException {
    client.stopDiscovery();
  }

  private void startTCPclient() throws IOException {
    client.linkWithTCPServer();
  }

  private void clientAuth() throws IOException {
    System.out.println("clientAuth command...");

    // Crea un oggetto Scanner per leggere l'input dell'utente
    Scanner scanner = new Scanner(System.in);

    String password;
    while (true) {
      System.out.print("Enter a 5-digit numeric password: ");
      password = scanner.nextLine().trim();

      // Controlla che la password sia composta solo da 5 cifre
      if (password.matches("\\d{5}")) {
        break; // Esci dal ciclo se la password è valida
      } else {
        System.out.println("Invalid password. Please enter exactly 5 digits.");
      }
    }

    // Invia la password al server
    client.sendMessage(password);
    System.out.println("Password sent to server: " + password);
  }

  private void sendC() throws IOException {
    System.out.println("sendC command...");
    server.sendMessage("Hello Client!"); // Send message
  }

  private void sendS() throws IOException {
    System.out.println("sendS command...");
    client.sendMessage("Hello Server!"); // Send message
  }

  private void forceWrap() {
    try {
      Thread.sleep(200);
      System.out.println();
    } catch (InterruptedException e) {
      //
    }
  }
}
