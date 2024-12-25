package quentin;

import java.io.IOException;
import java.util.Scanner;
import quentin.network.Client;
import quentin.network.Server;

public class OnlineGameParser {
    private Game game;
    Scanner scanner;
    Client client = new Client();
    Server server = new Server();
    Boolean isOnline = false;
    Boolean isWaiting = false;
    Boolean isServer = false;
    Boolean isClient = false;
    Thread waitAuth;
    String lastMessageReceived;

    public OnlineGameParser(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        System.out.println("Enter commands (type 'exit' to quit):");
        System.out.println(
                "Type 'startserver' if you want to host a match. Type 'startclient' if you want to"
                        + " join a match");

        final String coordinatePattern = "(?i)^[A-M](?:[0-9]|1[0-2])$";

        while (true) {
            printGamePrompt();
            if (!scanner.hasNextLine()) {
                continue;
            }
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "":
                    break;
                case "help":
                    showHelper();
                    break;
                case "exit":
                    scanner.close();
                    if (game != null) game.stop();
                    return;
                // for online game
                case "ss", "startserver", "starts":
                    startServer();
                    break;
                case "stopserver", "stops":
                    stopServer();
                    break;
                case "sc", "startclient", "startc":
                    startClient();
                    break;
                case "stopclient", "stopc":
                    stopClient();
                    break;
                case "clientauth", "clienta":
                    clientAuth();
                    break;
                default:
                    if (command.matches(coordinatePattern)) {
                        makeMove(command);
                    } else {
                        System.out.println("Unknown command: " + command);
                    }
                    break;
            }
        }
    }

    private void showHelper() {
        System.out.println("Available commands:");
        System.out.println("  exit                    Quits the game and exits the program");
        System.out.println("  help                    Shows this help");
        System.out.println(
                "  <coordinates>           Makes a move. Examples: A1 b2 C5 (wrong examples: 5A,"
                        + " 24)");
        System.out.println("ONLINE MODE");
        System.out.println("  back                    go back one move");
        System.out.println("  startserver or ss       Starts a server to play with other players");
        System.out.println("  stopserver or stops     Stops the server");
        System.out.println("  startclient or sc       Starts a client to play with other players");
        System.out.println("  stopclient or stopc     Stops the client");
        System.out.println("  clienta                 Authenticates the client");
    }

    private void printGamePrompt() {
        //        if (isOnline && isWaiting) {
        //            System.out.print("Waiting opponent player...");
        //        }
        System.out.print(" > ");
    }

    private void initialize() {
        game = new Game();
        if (!isOnline) return;
        System.out.println("New game started!");
        System.out.println(game.getBoard());
        if (isServer) {
            System.out.println("I'm server");
            isWaiting = false;
            System.out.print("It's your turn to play ");
        } else if (isClient) {
            System.out.println("I'm client");
            isWaiting = true;
            game.changeCurrentPlayer();
            waitMove();
        }
    }

    private void waitMove() {
        if (isClient) {
            System.out.println("Waiting for messages...");
            while (true) {
                String message = client.getMessageReceived();
                if (isMessageValid(message)) {
                    System.out.println("Message valid.");
                    break; // Exit when board is valid
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted while waiting for messages.");
                    return;
                }
            }
            System.out.println("out of while");
        } else if (isServer) {
            System.out.println("Waiting for messages...");
            while (true) {
                String message = server.getMessageReceived();
                if (isMessageValid(message)) {
                    System.out.println("Message valid.");
                    break; // Esce dal ciclo quando il messaggio è valido
                }
                try {
                    Thread.sleep(500); // Attendi un po' prima di riprovare
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted while waiting for messages.");
                    return; // Esci in caso di interruzione
                }
            }
            System.out.println("out of while");
        }
        System.out.println(game.getBoard());

        isWaiting = false;
        System.out.print("It's your turn to play ");
    }

    private void makeMove(String input) {
        System.out.println("makeMove command...");
        if (!game.isInProgress()) {
            System.out.println("There is not a game in progress");
            return;
        }
        if (isOnline && isWaiting) {
            System.out.print("Wait you turn ");
            waitMove();
            return;
        }
        if (isOnline && !isWaiting) {
            // your turn
            System.out.println("Your turn");
            char letter = input.charAt(0);
            int number = Integer.parseInt(input.substring(1));
            Cell cell = new Cell(number, game.letterToIndex(letter));
            try {
                game.place(cell);
            } catch (Exception e) {
                System.err.println("Invalid Coordinates");
                return;
            }
            game.coverTerritories(cell);
            if (game.currentPlayerHasWon()) {
                System.out.println(game.getCurrentPlayer() + " has won!");
                return;
            }
            sendBoard();
            isWaiting = !isWaiting; // swap turn
            System.out.print(game.getBoard());
            waitMove();
        }

        //        if (!isOnline) {
        //            game.changeCurrentPlayer();
        //            if (!game.canPlayerPlay()) {
        //                System.out.print("The next player " + game.getCurrentPlayer() + " can't
        // play");
        //                game.changeCurrentPlayer();
        //                System.out.println(", so the next player is " + game.getCurrentPlayer());
        //                return;
        //            }
        //        }
    }

    private void startServer() {
        isOnline = true;
        isServer = true;
        new Thread(
                        () -> {
                            System.out.println("Starting server...");
                            server.start();
                        })
                .start();

        Thread waitAuthOfClient =
                new Thread(
                        () -> {
                            while (!server.isClientAuth()) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    System.err.println(
                                            "Thread interrupted while waiting for client"
                                                    + " authentication");
                                    return; // Esce dal thread in caso di interruzione
                                }
                            }
                        });
        waitAuthOfClient.start();
        try {
            waitAuthOfClient.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }

    private void stopServer() {
        if (isOnline) {
            System.out.println("Stopping server...");
            server.stop();
            isServer = false;
        } else System.out.println("You can't stop a server in offline mode");
    }

    private void startClient() {
        isOnline = true;
        isClient = true;
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

        waitAuth =
                new Thread(
                        () -> {
                            while (!client.getAuthenticated()) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    System.err.println(
                                            "Thread interrupted while waiting for client"
                                                    + " authentication");
                                    return;
                                }
                            }
                        });
        waitAuth.start();
        System.out.println("type 'clientauth' to authenticate the client");
    }

    private void stopClient() {
        if (isOnline) client.stopDiscovery();
        else System.out.println("You can't stop a client in offline mode");
    }

    private void clientAuth() {
        if (!isOnline) {
            return;
        }
        //        Scanner scanner = new Scanner(System.in);
        //        String password;
        //        System.out.print("Enter a 5-digit numeric password: ");
        //        password = scanner.nextLine().trim();
        String password = "12345";
        client.trySendAuthentication(password);
        try {
            waitAuth.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }

    private void sendBoard() {
        if (!isOnline) return;
        if (isServer) server.sendMessage(game.board.toCompactString());
        else client.sendMessage(game.board.toCompactString());

        System.out.println("Board sent to " + (isServer ? "client" : "server"));
    }

    private Boolean isMessageValid(String compactBoard) {
        if (compactBoard == null) {
            // System.err.println("Received null message.");
            return false;
        }
        if (compactBoard.equals(lastMessageReceived)) {
            return false;
        }
        try {
            game.board.fromCompactString(compactBoard);
            System.err.println("Compact string accettata: " + compactBoard);
            lastMessageReceived = compactBoard;
            return true;
        } catch (Exception e) {
            if (!compactBoard.equals("Password accepted from TCP server"))
                System.err.println("Invalid board: " + compactBoard);
            return false;
        }
    }
}
