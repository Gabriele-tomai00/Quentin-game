package quentin.gui;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quentin.SettingHandler;
import quentin.cache.Cache;
import quentin.cache.GameLog;
import quentin.game.BoardPoint;
import quentin.game.Player;
import quentin.network.Client;
import quentin.network.NetworkHandler;
import quentin.network.Server;

public class GuiMain2 extends Application {
  private static final File CACHE_FILE = new File(".last_match_cache");
  private Cache<GameLog> cache;
  private BoardPoint color;
  private Parent root;

  public static void main(String... args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    if (cache != null && cache.getMemorySize() > 0 && cache.getLog() != null) {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Load.fxml"));
      fxmlLoader.setController(new LoaderController(cache));
      Parent root = fxmlLoader.load();
      primaryStage.setScene(new Scene(root));
      primaryStage.setTitle("Quentin");
      primaryStage.show();
    } else {
//            cache = new Cache<>();
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
//            loader.setController(new Controller(cache));
//            Parent root = loader.load();
      primaryStage.setTitle("Quentin");
      primaryStage.setScene(new Scene(root));
      primaryStage.setMinWidth(556);
      primaryStage.setMinHeight(516);
      primaryStage.show();
    }
  }

  @Override
  public void init() {
//        cache = CacheHandler.initialize(CACHE_FILE);
    run();
  }

  @Override
  public void stop() {
//        CacheHandler.saveCache(CACHE_FILE, cache);
  }

  public void run() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter commands (type 'exit' to quit):\n");
    System.out.println("Type 'startserver' if you want to host a match. Type 'startclient' if you want to"
        + " join a match\n");
//    while (true) {
      try {
        printGamePrompt();
//        if (!scanner.hasNextLine()) { continue; }
        String command = scanner.nextLine()
                                .trim()
                                .toLowerCase();
        switch (command) {
        case ""                  -> { break; }
        case "help"              -> showHelper();
        case "exit"              -> { return; }
        case "ss", "startserver" -> startServer();
        case "stop"              -> stop();
        case "sc", "startclient" -> startClient();
        default                  -> System.err.println("Unkown command: " + command);
        }
      } catch (RuntimeException e) {
        System.out.println(e.getMessage());
        return;
//      }
    }
  }

  public void showHelper() {
    System.out.println("Available commands:\n");
    String[][] commands = { { "exit", "Quits the game and exits the program" }, { "help", "Shows this help" },
        { "setusername", "Set a username to be recognized by other players when playing online" },
        { "setport",
            "Set a different TCP port, in case it is already used (IMPORTANT: the other player"
                + " must know the new port)" },
        { "getusername or getu", "Prints you current username" },
        { "getport or getp", "Prints the currently used TCP port (your opposing player may need it)" },
        { "startserver or ss", "Starts a server to play with other players" },
        { "startclient or sc", "Starts a client to play with other players" }, };
    for (String[] strings : commands) { System.out.printf("  %-25s: %-40s%n", strings[0], strings[1]); }
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void start(Socket socket) {
    OnlineGuiGame game = new OnlineGuiGame(new Player(color));
    NetworkHandler handler = new NetworkHandler(socket, game);
    try {
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(handler);
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Main2.fxml"));
      loader.setController(new OnlineController(handler, game));
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
