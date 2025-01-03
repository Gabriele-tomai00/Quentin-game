package quentin.network;

import java.io.IOException;
import java.util.Scanner;
import quentin.game.*;

public class OnlineGameParser extends SimpleGameStarter {
    private LocalGame game;
    Scanner scanner;
    Client client = new Client();
    Server server = new Server();
    Boolean isOnline = false;
    Boolean isWaiting = false;
    Boolean isServer = false;
    Boolean isClient = false;
    String lastMessageReceived;
    String CLEAR = "\033[H\033[2J";

    public OnlineGameParser(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void startDisplay() {
        //
    }

    @Override
    public void displayWinner() {
        System.out.println(
                CLEAR + String.format("%s has won", game.getCurrentPlayer()).toUpperCase());
    }

    @Override
    public void display() {
        System.out.println(CLEAR + game.getBoard());
    }

    public void run() {
        System.out.println("Enter commands (type 'exit' to quit):");
        System.out.println(
                "Type 'startserver' if you want to host a match. Type 'startclient' if you want"
                        + " to join a match");

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
                    return;
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
                    makeMove(command);
                    //                    } else {
                    //                        System.out.println("Unknown command: " + command);
                    //                    }
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
        // if (isOnline && isWaiting) {
        // System.out.print("Waiting opponent player...");
        // }
        System.out.print(" > ");
    }

    @Override
    public void start() {
        game = new LocalGame();
        if (!isOnline
                || (isServer && !server.isClientAuth())
                || isClient && !client.isAuthenticated()) return;
        displayMessage("New game started!");
        display();
        // System.out.println(game.getBoard());
        if (isServer) {
            displayMessage("I'm server");
            isWaiting = false;
            System.out.print("It's your turn to play > ");
        } else if (isClient) {
            displayMessage("I'm client");
            game.changeCurrentPlayer();
            waitMove();
        }
    }

    private void waitMove() {
        Thread threadWaitMove =
                new Thread(
                        () -> {
                            isWaiting = true;
                            displayMessage("Waiting for messages...");
                            if (isClient) {
                                while (true) {
                                    String message = client.getMessageReceived();
                                    if (isMessageValid(message)) {
                                        displayMessage("Message valid.");
                                        break; // Exit when board is valid
                                    }
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        System.err.println(
                                                "Thread interrupted while waiting for messages.");
                                        return;
                                    }
                                }
                            } else if (isServer) {
                                while (true) {
                                    String message = server.getMessageReceived();
                                    if (isMessageValid(message)) {
                                        displayMessage("Message valid.");
                                        break;
                                    }
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        System.err.println(
                                                "Thread interrupted while waiting for messages.");
                                        return;
                                    }
                                }
                            }
                            display();
                            if (!game.canPlayerPlay()) {
                                displayMessage(
                                        CLEAR
                                                + String.format(
                                                                "You/The %s player can't play",
                                                                game.getCurrentPlayer())
                                                        .toUpperCase());

                                waitMove();
                            } else {
                                isWaiting = false;
                                System.out.print("It's your turn to play ");
                            }
                        });
        threadWaitMove.start();
    }

    @Override
    public void makeMove(String input) {
        if ((isServer && !server.isClientAuth()) || isClient && !client.isAuthenticated()) {
            displayMessage("Client is not yet authenticated");
            return;
        }

        if (!isOnline) {
            displayMessage("There is no online game");
            return;
        }
        if (isWaiting) {
            System.out.print("Wait you turn ");
            waitMove();
        } else {
            // your turn
            Cell cell;
            try {
                cell = new MoveParser(input).parse();
                game.place(cell);
            } catch (Exception e) {
                System.err.println("Invalid Coordinates");
                return;
            }
            game.coverTerritories(cell);

            if (hasWon()) return;

            sendBoard();
            System.out.print(game.getBoard());

            if (!game.canPlayerPlay()) {
                displayMessage(
                        CLEAR
                                + String.format(
                                                "You/The %s player can't play",
                                                game.getCurrentPlayer())
                                        .toUpperCase());
                displayMessage("It' your turn again");
            } else waitMove();
        }

        if (!isOnline) {
            game.changeCurrentPlayer();
            if (!game.canPlayerPlay()) {
                displayMessage(
                        CLEAR
                                + String.format(
                                                "You/The %s player can't play",
                                                game.getCurrentPlayer())
                                        .toUpperCase());
                game.changeCurrentPlayer();
                displayMessage(", so the next player is " + game.getCurrentPlayer());
            }
        }
    }

    public boolean hasWon() {
        if (game.hasWon(game.getCurrentPlayer())) {
            displayWinner();
            return true;
        }
        game.changeCurrentPlayer();
        if (game.hasWon(game.getCurrentPlayer())) {
            displayWinner();
            return true;
        }
        game.changeCurrentPlayer();
        return false;
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
                                if (!isOnline) {
                                    return;
                                }
                            }
                            isOnline = true;
                            start();
                        });
        waitAuthOfClient.start();
        try {
            waitAuthOfClient.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        start();
    }

    private void stopServer() {
        displayMessage("Stopping server...");
        server.stop();
        isServer = false;
        isOnline = false;
    }

    private void stopClient() {
        if (isOnline) {
            client.stopDiscovery();
            isOnline = false;
        } else displayMessage("You can't stop a client in offline mode");
    }

    private void startClient() {
        isClient = true;
        new Thread(
                        () -> {
                            displayMessage("Starting client...");
                            try {
                                client.startDiscovery();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .start();

        isOnline = true;
        displayMessage("Type 'clienta' to insert the password");
    }

    private void clientAuth() {
        int attempts = 3;
        String password;
        while (true) {
            if (attempts == 0) return;

            System.out.println("attempts: " + attempts);
            System.out.print("password > ");
            password = scanner.nextLine().trim();
            if ((password.length() != 5 || !password.matches("\\d{5}"))) {
                System.out.println("Invalid password, retry ");
                attempts--;
                continue;
            }
            client.sendAuthentication(password);
            if (waitServerAuthenticationResponse()) break;
            else attempts--;
        }
        start();
    }

    private boolean waitServerAuthenticationResponse() {
        System.out.println("Wait answer...");
        long startTime = System.currentTimeMillis();
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (System.currentTimeMillis() - startTime > 3000) {
                System.err.println("Timeout reached. Authentication not completed.");
                return false;
            }

            if (client.getStateAuthentiation() == State.authenticated) {
                System.out.println("Authentication completed! ");
                return true;
            }
            if (client.getStateAuthentiation() == State.failedAuthentication) {
                System.out.println("Authentication failed! ");
                return false;
            }
        }
    }

    private void sendBoard() {
        if (!isOnline) return;
        if (isServer) server.sendMessage(game.getBoard().toCompactString());
        else client.sendMessage(game.getBoard().toCompactString());

        displayMessage("Board sent to " + (isServer ? "client" : "server"));
    }

    private Boolean isMessageValid(String compactBoard) {
        if (compactBoard == null) {
            return false;
        }
        if (compactBoard.equals(lastMessageReceived)) {
            return false;
        }
        try {
            game.getBoard().fromCompactString(compactBoard);
            System.err.println("Compact string accettata: " + compactBoard);
            lastMessageReceived = compactBoard;
            return true;
        } catch (Exception e) {
            if (!compactBoard.equals("Password accepted from TCP server"))
                System.err.println("Invalid board: " + compactBoard);
            return false;
        }
    }

    @Override
    public void displayMessage(String format) {
        System.out.println(format);
    }
}
