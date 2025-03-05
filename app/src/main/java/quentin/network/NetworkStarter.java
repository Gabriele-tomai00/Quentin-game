package quentin.network;

import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import quentin.SettingHandler;
import quentin.exceptions.InvalidCellValuesException;
import quentin.exceptions.MoveException;

public class NetworkStarter {
    SettingHandler settingHandler = new SettingHandler();
    private ExecutorService executor;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter commands (type 'exit' to quit):\n");
        System.out.println(
                "Type 'startserver' if you want to host a match. Type 'startclient' if you want to"
                        + " join a match\n");
        while (true) {
            try {
                printGamePrompt();
                if (!scanner.hasNextLine()) {
                    continue;
                }
                String command = scanner.nextLine().trim().toLowerCase();
                switch (command) {
                    case "help" -> showHelper();
                    case "exit" -> {
                        return;
                    }
                    case "getusername", "getu" ->
                            System.out.println(
                                    "You current username is: "
                                            + settingHandler.getUsername()
                                            + "\n");
                    case "getport", "getp" ->
                            System.out.println(
                                    "You current username is: " + settingHandler.getPort() + "\n");
                    case "setusername", "setu" -> setUsername(scanner);
                    case "setport", "setp" -> setTCPport(scanner);
                    case "ss", "startserver", "starts" -> startServer();
                    case "stop" -> stop();
                    case "sc", "startclient", "startc" -> startClient();
                    default -> {
                        break;
                    }
                }
            } catch (MoveException | InvalidCellValuesException e) {
                System.out.println(e.getMessage() + "\n");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    private void setTCPport(Scanner scanner) {
        System.out.println(
                "Enter new TCP port (IMPORTANT: the other player must know the new port): ");
        int port = Integer.parseInt(scanner.nextLine());
        settingHandler.setPort(port);
    }

    private void setUsername(Scanner scanner) {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        settingHandler.setUsername(username);
    }

    public void showHelper() {
        System.out.println("Available commands:\n");
        String[][] commands = {
            {"exit", "Quits the game and exits the program"},
            {"help", "Shows this help"},
            {"<coordinates>", "Makes a move. Examples: A1 b2 C5 (wrong examples: 5A, 24)"},
            {"setusername", "Set a username to be recognized by other players when playing online"},
            {
                "setport",
                "Set a different TCP port, in case it is already used (IMPORTANT: the other player"
                        + " must know the new port)"
            },
            {"getusername or getu", "Prints you current username"},
            {
                "getport or getp",
                "Prints the currently used TCP port (your opposing player may need it)"
            },
            {"startserver or ss", "Starts a server to play with other players"},
            {"stopserver or stops", "Stops the server"},
            {"startclient or sc", "Starts a client to play with other players"},
            {"stopclient or stopc", "Stops the client"},
            {"clienta", "Authenticates the client"}
        };
        for (String[] strings : commands) {
            System.out.printf("  %-25s: %-40s%n", strings[0], strings[1]);
        }
    }

    private void printGamePrompt() {
        System.out.println("QuentinGame - online mode > ");
    }

    private void startServer() {
        Server server = new Server(new SettingHandler());
        executor = Executors.newSingleThreadExecutor();
        Future<Socket> future = executor.submit(server);
        //    NetworkHandler handler = new NetworkHandler(future.get(), new OnlineGame(new
        // Player(BoardPoint.BLACK)));
        //    new Thread(handler).start();
    }

    private void startClient() {
        Client client = new Client();
        executor.submit(client);
        //    TODO implement client start
    }

    private void stop() {
        executor.shutdown();
        executor.shutdownNow();
    }

    private void sleepSafely(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while waiting for client" + " authentication");
        }
    }
}
