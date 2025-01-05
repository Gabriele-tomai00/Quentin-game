package quentin.network;

import java.io.IOException;
import java.util.Scanner;
import quentin.SettingHandler;

public class UDPparser {
    SettingHandler settingHandler = new SettingHandler();
    UDPServer udpServer = new UDPServer(settingHandler.getUsername(), settingHandler.getPort());
    UDPClient udpClient = new UDPClient();

    public UDPparser() throws IOException {}

    public static void main(String[] args) throws IOException, InterruptedException {
        UDPparser parser = new UDPparser();
        parser.run();
    }

    public void run() throws InterruptedException, IOException {
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
                            udpServer.startServer();
                        })
                .start();
    }

    private void stopServer() throws InterruptedException {
        System.out.println("Stopping server...");
        udpServer.stopServer();
    }

    private void startClient() {
        new Thread(
                        () -> {
                            System.out.println("Starting client...");
                            udpClient.startDiscovery();
                        })
                .start();
    }

    private void stopClient() throws InterruptedException {
        udpClient.stopDiscovery();
    }
}
