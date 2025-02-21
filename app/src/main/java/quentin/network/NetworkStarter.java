package quentin.network;

import java.util.List;
import java.util.Scanner;

import quentin.SettingHandler;
import quentin.exceptions.InvalidCellValuesException;
import quentin.exceptions.MoveException;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.Game;
import quentin.game.GameBoard;
import quentin.game.GameStarter;
import quentin.game.MoveParser;
import quentin.game.Player;

public class NetworkStarter extends GameStarter {
  private OnlineGame game;
  Client client;
  Server server;
  Boolean isOnline = false;
  Boolean isWaiting = false;
  Boolean isServer = false;
  Boolean isClient = false;
  String lastBoardReceived;
  SettingHandler settingHandler = new SettingHandler();

  public void run() {
    Scanner scanner = new Scanner(System.in);
    displayMessage("Enter commands (type 'exit' to quit):\n");
    displayMessage("Type 'startserver' if you want to host a match. Type 'startclient' if you want to"
        + " join a match\n");
    while (true) {
      try {
        printGamePrompt();
        if (!scanner.hasNextLine()) { continue; }
        String command = scanner.nextLine()
                                .trim()
                                .toLowerCase();
        switch (command) {
        case "" -> { break; }
        case "help" -> showHelper();
        case "exit" -> {
          if (isServer) stopServer();
          else stopClient();
          return;
        }
        case "getusername", "getu" -> displayMessage("You current username is: " + settingHandler.getUsername() + "\n");
        case "getport", "getp" -> displayMessage("You current username is: " + settingHandler.getPort() + "\n");
        case "setusername", "setu" -> setUsername(scanner);
        case "setport", "setp" -> setTCPport(scanner);
        case "ss", "startserver", "starts" -> startServer();
        case "stopserver", "stops" -> stopServer();
        case "sc", "startclient", "startc" -> startClient();
        case "stopclient", "stopc" -> stopClient();
        case "clientauth", "clienta" -> clientAuth(scanner);
        default -> makeMove(command);
        }
      } catch (MoveException | InvalidCellValuesException e) {
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
      displayMessage("Enter new TCP port (IMPORTANT: the other player must know the new port): ");
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

  public void showHelper() {
    displayMessage("Available commands:\n");
    String[][] commands = { { "exit", "Quits the game and exits the program" }, { "help", "Shows this help" },
        { "<coordinates>", "Makes a move. Examples: A1 b2 C5 (wrong examples: 5A, 24)" },
        { "setusername", "Set a username to be recognized by other players when playing online" },
        { "setport",
            "Set a different TCP port, in case it is already used (IMPORTANT: the other player"
                + " must know the new port)" },
        { "getusername or getu", "Prints you current username" },
        { "getport or getp", "Prints the currently used TCP port (your opposing player may need it)" },
        { "startserver or ss", "Starts a server to play with other players" },
        { "stopserver or stops", "Stops the server" },
        { "startclient or sc", "Starts a client to play with other players" },
        { "stopclient or stopc", "Stops the client" }, { "clienta", "Authenticates the client" } };
    for (String[] strings : commands) { System.out.printf("  %-25s: %-40s%n", strings[0], strings[1]); }
  }

  private void printGamePrompt() {
    if (!isWaiting) displayMessage("QuentinGame - online mode > ");
    else displayMessage("wait your turn or quit (exit command) \n");
  }

  public void start() {
    if (!isOnline || (isServer && !server.isClientAuth()) || isClient && !client.isAuthenticated()) { return; }
    displayMessage("New game started!\n");
    if (isServer) {
      game = new OnlineGame(new Player(BoardPoint.BLACK));
      isWaiting = false;
    } else if (isClient) {
      game = new OnlineGame(new Player(BoardPoint.WHITE));
      waitMove();
    }
    display();
  }

  private void waitMove() {
    Thread threadWaitMove = new Thread(() -> {
      isWaiting = true;
      displayMessage("Waiting for messages...\n");
      if (isClient) {
        while (isOnline) {
          String boardReceived = client.getBoardReceived();
          if (boardReceived != null && boardReceived.equals("quit")) {
            client.stop();
            return;
          } else if (isBoardValid(boardReceived)) break;
          sleepSafely(500);
        }
      }
      if (isServer) {
        while (isOnline) {
          String boardReceived = server.getBoardReceived();
          if (boardReceived != null && boardReceived.equals("quit")) {
            server.stop();
            return;
          } else if (isBoardValid(boardReceived)) break;
          sleepSafely(500);
        }
      }
      display();
      if (hasWon()) { return; }
      isWaiting = false;
      displayMessage("It's your turn to play: ");
    });
    threadWaitMove.start();
  }

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
      if (isClient && input.equals("pie") && game.isFirstMove()) {
        game.setFirstMove(false);
        displayMessage("Now you are black! Wait messages\n");
        game.applyPieRule();
        sendBoard();
        waitMove();
        return;
      }
      if (isServer && input.equals("pie")) {
        displayMessage("Invalid move in this round\n");
        return;
      }
      Cell cell = new MoveParser(input).parse();
      game.place(cell);
      game.coverTerritories(cell);
      if (hasWon()) {
        sendBoard();
        display();
        return;
      }

      display();

      if (!game.canPlayerPlay()) {
        displayMessage(CLEAR + "You can play again (the opponent player can't play)");
      } else {
        sendBoard();
        waitMove();
      }
    }
  }

  public boolean hasWon() {
    if (game.hasWon(new Player(BoardPoint.BLACK))) {
      displayWinner();
      return true;
    }
    if (game.hasWon(new Player(BoardPoint.WHITE))) {
      displayWinner();
      return true;
    }
    return false;
  }

  private void startServer() {
    server = new Server();
    isOnline = true;
    isServer = true;
    new Thread(() -> {
      displayMessage("Starting server...\n");
      server.start();
    }).start();

    Thread waitAuthOfClient = new Thread(() -> {
      while (!server.isClientAuth()) {
        sleepSafely(100);
        if (!isOnline) { return; }
      }
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
    new Thread(() -> {
      displayMessage("Starting client...");
      client.startDiscovery();
    }).start();

    isOnline = true;
    new Thread(() -> {
      while (!client.isServerFound()) { sleepSafely(500); }
      displayMessage("\nType 'clienta' to insert the password\n");
    }).start();
  }

  private void clientAuth(Scanner scanner) throws InterruptedException {
    int attempts = 3;
    String password;
    while (isOnline) {
      if (attempts == 0) return;

      displayMessage("attempts: " + attempts + "\n");
      displayMessage("password > ");
      password = scanner.nextLine()
                        .trim();
      if ((password.equals("exit"))) { return; }
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
    while (isOnline) {
      Thread.sleep(500);

      if (System.currentTimeMillis() - startTime > 3000) {
        System.err.println("Timeout reached. Authentication not completed.");
        return false;
      }

      if (client.getStateAuthentication() == ClientAuthState.AUTHENTICATED) { return true; }
      if (client.getStateAuthentication() == ClientAuthState.FAILED_AUTHENTICATION) {
        displayMessage("Authentication failed! \n");
        return false;
      }
    }
    return false;
  }

  private void sendBoard() {
    if (!isOnline) return;
    if (isServer) server.sendMessage(game.getBoard()
                                         .toCompactString());
    else client.sendMessage(game.getBoard()
                                .toCompactString());
    displayMessage("Board sent to " + (isServer ? "client" : "server") + "\n");
  }

  private Boolean isBoardValid(String compactBoard) {
    if (game == null || compactBoard == null || compactBoard.equals(lastBoardReceived)) { return false; }

    if (game.getBoard()
            .toCompactString()
            .equals(compactBoard)) { // pie rule
      displayMessage("Your opponent player used the pie rule, now you are White!\n");
      game.applyPieRule();
    } else {
      game.updateBoard(new GameBoard(compactBoard));
    }
    lastBoardReceived = compactBoard;
    return true;
  }

  private void sleepSafely(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread()
            .interrupt();
      System.err.println("Thread interrupted while waiting for client" + " authentication");
    }
  }

  @Override
  public Game getGame() {
    return game;
  }

  @Override
  public void display() {
    if (isOnline) {
      System.out.println(game.getBoard());
      List<Cell> moves = game.getLastMoves();
      if (!moves.isEmpty()) {
        if (moves.size() > 1) {
          System.out.println("Last moves of the opponent player are: " + game.getLastMoves());
        } else {
          System.out.println("Last move of the opponent player is : " + game.getLastMoves()
                                                                            .get(0));
        }
      }
    }
  }
}
