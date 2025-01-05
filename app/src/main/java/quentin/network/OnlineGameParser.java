package quentin.network;

import java.util.Scanner;
import quentin.SettingHandler;
import quentin.game.Cell;
import quentin.game.LocalGame;
import quentin.game.MoveParser;
import quentin.game.SimpleGameStarter;

public class OnlineGameParser extends SimpleGameStarter {
    private LocalGame game;
    Client client;
    Server server;
    Boolean isOnline = false;
    Boolean isWaiting = false;
    Boolean isServer = false;
    Boolean isClient = false;
    String lastBoardReceived;
    SettingHandler settingHandler = new SettingHandler();
    String CLEAR = "\033[H\033[2J";

    @Override
    public void displayWinner() {
        System.out.println(
                CLEAR + String.format("%s has won", game.getCurrentPlayer()).toUpperCase());
    }

    @Override
    public void display() {
        System.out.println(CLEAR + game.getBoard());
    }

    public void run(Scanner scanner) {
        System.out.println("Enter commands (type 'exit' to quit):");
        System.out.println(
                "Type 'startserver' if you want to host a match. Type 'startclient' if you want"
                        + " to join a match");

        while (true) {
            try {
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
                        if (isServer) stopServer();
                        else stopClient();
                        return;
                    case "getusername", "getu":
                        System.out.println(
                                "You current username is: " + settingHandler.getUsername());
                        break;
                    case "getport", "getp":
                        System.out.println("You current username is: " + settingHandler.getPort());
                        break;
                    case "setusername", "setu":
                        setUsername(scanner);
                        break;
                    case "setport", "setp":
                        setTCPport(scanner);
                        break;

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
                        clientAuth(scanner);
                        break;
                    default:
                        makeMove(command);
                        // } else {
                        // System.out.println("Unknown command: " + command);
                        // }
                        break;
                }
            } catch (RuntimeException e) {
                displayMessage(e.getMessage());
                return;
            } catch (InterruptedException e) {
                displayMessage("Interrupted thread");
                return;
            }
        }
    }

    private void setTCPport(Scanner scanner) {
        if (!isOnline) {
            System.out.println(
                    "Enter new TCP port (IMPORTANT: the other player must know the new port): ");
            int port = Integer.parseInt(scanner.nextLine());
            settingHandler.setPort(port);
        } else System.out.println("You are already online, you can't change parameters");
    }

    private void setUsername(Scanner scanner) {
        if (!isOnline) {
            System.out.println("Enter username: ");
            String username = scanner.nextLine();
            settingHandler.setUsername(username);
        } else System.out.println("You are already online, you can't change parameters");
    }

    private void showHelper() {
        System.out.println("Available commands:");
        System.out.println("  exit                    Quits the game and exits the program");
        System.out.println("  help                    Shows this help");
        System.out.println(
                "  <coordinates>           Makes a move. Examples: A1 b2 C5 (wrong examples: 5A,"
                        + " 24)");
        System.out.println("ONLINE MODE");
        System.out.println("  back                    Go back one move");
        System.out.println(
                "  setusername             Set a username to be recognized by other players when"
                        + " playing online");
        System.out.println(
                "  setport                 Set a different TCP port, in case it is already used"
                        + " (IMPORTANT: the other player must know the new port)");
        System.out.println("  getusername or getu     Get you current username");
        System.out.println(
                "  getport or getp         Get the current TCP port (your opposing player may need"
                        + " it)");
        System.out.println("  startserver or ss       Starts a server to play with other players");
        System.out.println("  stopserver or stops     Stops the server");
        System.out.println("  startclient or sc       Starts a client to play with other players");
        System.out.println("  stopclient or stopc     Stops the client");
        System.out.println("  clienta                 Authenticates the client");
    }

    private void printGamePrompt() {
        if (!isWaiting) System.out.print("QuentinGame - online mode > ");
        else System.out.println("wait your turn or quit");
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
                                    String boardReceived = client.getBoardReceived();
                                    if (isBoardValid(boardReceived)) break;
                                    sleepSafely(500);
                                }
                            } else if (isServer) {
                                while (true) {
                                    String message = server.getMessageReceived();
                                    if (isBoardValid(message)) break;
                                    sleepSafely(500);
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
            waitMove();
        } else {
            // your turn
            Cell cell;
            cell = new MoveParser(input).parse();
            game.place(cell);
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
        server = new Server();
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
                                sleepSafely(100);
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
        }
    }

    private void startClient() {
        client = new Client();
        isClient = true;
        new Thread(
                        () -> {
                            displayMessage("Starting client...");
                            client.startDiscovery();
                        })
                .start();

        isOnline = true;
        sleepSafely(1000);
        displayMessage("\nType 'clienta' to insert the password\n");
    }

    private void clientAuth(Scanner scanner) throws InterruptedException {
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

    private boolean waitServerAuthenticationResponse() throws InterruptedException {
        System.out.println("Wait answer...");
        long startTime = System.currentTimeMillis();
        while (true) {
            Thread.sleep(500);

            if (System.currentTimeMillis() - startTime > 3000) {
                System.err.println("Timeout reached. Authentication not completed.");
                return false;
            }

            if (client.getStateAuthentication() == State.authenticated) {
                return true;
            }
            if (client.getStateAuthentication() == State.failedAuthentication) {
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

    private Boolean isBoardValid(String compactBoard) {
        if (compactBoard == null || compactBoard.equals(lastBoardReceived)) return false;
        game.getBoard().fromCompactString(compactBoard);
        lastBoardReceived = compactBoard;
        return true;
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
