package quentin.network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import quentin.game.BoardPoint;
import quentin.game.OnlineGame;
import quentin.game.OnlineGameStarter;

public class NetworkStarter implements Runnable {

    private final SettingHandler handler = new SettingHandler();
    private BoardPoint color;
    private ExecutorService executor;
    private final Scanner scanner = new Scanner(System.in);
    private boolean started = true;

    @Override
    public void run() {
        System.out.println("Enter commands (type 'exit' to quit):\n");
        System.out.println(
                "Type 'startserver' if you want to host a match. Type 'startclient' if you want to"
                        + " join a match\n");
        while (started) {
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
                    case "getusername", "getu" -> System.out.println(handler.getUsername());
                    case "getport", "getp" -> System.out.println(handler.getPort());
                    case "setusername", "setu" -> setUsername();
                    case "setport", "setp" -> setport();
                    case "ss", "startserver" -> startServer();
                    case "stop" -> stop();
                    case "sc", "startclient" -> startClient();
                    default -> System.err.println("Unknown command: " + command);
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void setport() {
        System.out.println("Enter new port: ");
        int port = Integer.parseInt(scanner.nextLine());
        handler.setPort(port);
    }

    private void setUsername() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        handler.setUsername(username);
    }

    private void showHelper() {
        System.out.println("Available commands:\n");
        String[][] commands = {
            {"exit", "Quits the game and exits the program"},
            {"help", "Shows this help"},
            {"setusername", "Set a username to be recognized by other players when playing online"},
            {
                "setport",
                "Set a new port, in case it is already used (IMPORTANT: the other player"
                        + " must know the new port)"
            },
            {"getusername or getu", "Prints you current username"},
            {
                "getport or getp",
                "Prints the currently used TCP port (your opposing player may need it)"
            },
            {"startserver or ss", "Starts a server to play with other players"},
            {"startclient or sc", "Starts a client to play with other players"},
        };
        for (String[] strings : commands) {
            System.out.printf("  %-25s: %-40s%n", strings[0], strings[1]);
        }
    }

    private void printGamePrompt() {
        System.out.print("QuentinGame - online mode > ");
    }

    private void startServer() {
        try {
            color = BoardPoint.BLACK;
            Server server = new Server(handler);
            Socket socket = server.call();
            start(socket);
        } catch (IOException e) {
          System.err.println(e.getMessage());
        }
    }

    private void startClient() {
        try {
            color = BoardPoint.WHITE;
            Client client = new Client(handler);
            Socket socket = client.call();
            start(socket);
        } catch (IOException e) {
          System.err.println(e.getMessage());
        }
    }

    protected void start(Socket socket) {
        OnlineGame game = new OnlineGame(color);
        NetworkHandler networkHandler = new NetworkHandler(socket, game);
        OnlineGameStarter starter = new OnlineGameStarter(networkHandler, game);
        executor = Executors.newSingleThreadExecutor();
        executor.submit(networkHandler);
        starter.run();
        stop();
    }

    private void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public SettingHandler getHandler() {
        return handler;
    }

    public void alreadyStarted() {
        started = false;
    }

    public BoardPoint getColor() {
        return color;
    }
}
