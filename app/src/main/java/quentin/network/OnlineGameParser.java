package quentin.network;

import java.util.List;
import java.util.Scanner;
import quentin.SettingHandler;
import quentin.exceptions.InvalidCellValuesException;
import quentin.game.*;

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
        displayMessage(
                CLEAR + String.format("%s has won", game.getCurrentPlayer()).toUpperCase() + "\n");
    }

    @Override
    public void display() {
        System.out.println(CLEAR + game.getBoard());
        List<Cell> lastMoves = game.getLastMoves();
        if (lastMoves != null && !lastMoves.isEmpty()) {
            if (lastMoves.size() == 1)
                displayMessage("The last stone pleased is " + lastMoves.get(0) + "\n");
            else displayMessage("The last stones pleased are " + lastMoves + "\n");
        }
    }

    public void run(Scanner scanner) {
        displayMessage("Enter commands (type 'exit' to quit):\n");
        displayMessage(
                "Type 'startserver' if you want to host a match. Type 'startclient' if you want"
                        + " to join a match\n");

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
                        displayMessage(
                                "You current username is: " + settingHandler.getUsername() + "\n");
                        break;
                    case "getport", "getp":
                        displayMessage(
                                "You current username is: " + settingHandler.getPort() + "\n");
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
                        break;
                }
            } catch (InvalidCellValuesException e) {
                displayMessage(e.getMessage() + "\n");
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
            displayMessage(
                    "Enter new TCP port (IMPORTANT: the other player must know the new port): ");
            int port = Integer.parseInt(scanner.nextLine());
            settingHandler.setPort(port);
        } else displayMessage("You are already online, you can't change parameters\n");
    }

    private void setUsername(Scanner scanner) {
        if (!isOnline) {
            displayMessage("Enter username: ");
            String username = scanner.nextLine();
            settingHandler.setUsername(username);
        } else displayMessage("You are already online, you can't change parameters\n");
    }

    private void showHelper() {
        displayMessage("Available commands:\n");
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
            System.out.printf("  %-25s: %-40s", strings[0], strings[1]);
        }
    }

    private void printGamePrompt() {
        if (!isWaiting) displayMessage("QuentinGame - online mode > ");
        else displayMessage("wait your turn or quit\n");
    }

    @Override
    public void start() {
        game = new LocalGame();
        if (!isOnline
                || (isServer && !server.isClientAuth())
                || isClient && !client.isAuthenticated()) return;
        displayMessage("New game started!");
        display();
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
                                    if (boardReceived != null && boardReceived.equals("quit")) {
                                        client.stop();
                                        return;
                                    } else if (isBoardValid(boardReceived)) break;
                                    sleepSafely(500);
                                }
                            }
                            if (isServer) {
                                while (true) {
                                    String boardReceived = server.getMessageReceived();
                                    if (boardReceived != null && boardReceived.equals("quit")) {
                                        server.stop();
                                        return;
                                    } else if (isBoardValid(boardReceived)) break;
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
                                displayMessage("It's your turn to play ");
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
                            displayMessage("Starting server...\n");
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
            client.stop();
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

            displayMessage("attempts: " + attempts + "\n");
            displayMessage("password > ");
            password = scanner.nextLine().trim();
            if ((password.length() != 5 || !password.matches("\\d{5}"))) {
                displayMessage("Invalid password, retry\n");
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
        displayMessage("Wait answer...\n");
        long startTime = System.currentTimeMillis();
        while (true) {
            Thread.sleep(500);

            if (System.currentTimeMillis() - startTime > 3000) {
                System.err.println("Timeout reached. Authentication not completed.");
                return false;
            }

            if (client.getStateAuthentication() == ClientAuthState.authenticated) {
                return true;
            }
            if (client.getStateAuthentication() == ClientAuthState.failedAuthentication) {
                displayMessage("Authentication failed! \n");
                return false;
            }
        }
    }

    private void sendBoard() {
        if (!isOnline) return;
        if (isServer) server.sendMessage(game.getBoard().toCompactString());
        else client.sendMessage(game.getBoard().toCompactString());

        displayMessage("Board sent to " + (isServer ? "client\n" : "server\n"));
    }

    private Boolean isBoardValid(String compactBoard) {
        if (compactBoard == null || compactBoard.equals(lastBoardReceived)) return false;
        // game.getBoard().fromCompactString(compactBoard);
        game.updateBoard(new Board(compactBoard));
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
