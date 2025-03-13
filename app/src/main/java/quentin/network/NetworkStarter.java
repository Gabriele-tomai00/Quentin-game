package quentin.network;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import quentin.SettingHandler;
import quentin.game.BoardPoint;
import quentin.game.Player;

public class NetworkStarter {

    private SettingHandler settingHandler = new SettingHandler();
    private BoardPoint color;
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
                    case "" -> {
                        break;
                    }
                    case "help" -> showHelper();
                    case "exit" -> {
                        return;
                    }
                    case "getusername", "getu" -> System.out.println(settingHandler.getUsername());
                    case "getport", "getp" -> System.out.println(settingHandler.getPort());
                    case "setusername", "setu" -> setUsername(scanner);
                    case "setport", "setp" -> setTCPport(scanner);
                    case "ss", "startserver" -> startServer();
                    case "stop" -> stop();
                    case "sc", "startclient" -> startClient();
                    default -> System.err.println("Unkown command: " + command);
                }
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
            {"startclient or sc", "Starts a client to play with other players"},
            {"start", "Starts the match after successful authentication"}
        };
        for (String[] strings : commands) {
            System.out.printf("  %-25s: %-40s%n", strings[0], strings[1]);
        }
    }

    private void printGamePrompt() {
        System.out.println("QuentinGame - online mode > ");
    }

    private void startServer() {
        color = BoardPoint.BLACK;
        Server server = new Server(new SettingHandler());
        Socket socket = server.call();
        start(socket);
    }

    private void startClient() {
        try {
            color = BoardPoint.WHITE;
            Client client = new Client();
            Socket socket = client.call();
            start(socket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void start(Socket socket) {
        OnlineGame game = new OnlineGame(new Player(color));
        NetworkHandler handler = new NetworkHandler(socket, game);
        OnlineGameStarter starter = new OnlineGameStarter(handler, game);
        executor = Executors.newSingleThreadExecutor();
        executor.submit(handler);
        starter.run();
    }

    private void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
        if (executor != null) {
            executor.shutdown();
        }
    }
}
