package quentin;

import java.io.File;
import java.util.Scanner;
import javafx.application.Application;
import quentin.cache.CacheHandler;
import quentin.game.CachedGameStarter;
import quentin.gui.GuiLauncher;
import quentin.gui.OnlineGuiLauncher;
import quentin.network.NetworkStarter;

public class Main {

    private static final File CACHE_FILE = new File(".last_match_cache");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(
                """
                  ____  _    _            _   _          _____
                 / __ \\| |  | |          | | (_)        / ____|
                | |  | | |  | | ___ _ __ | |_ _ _ __   | |  __  __ _ _ __ ___   ___
                | |  | | |  | |/ _ \\ '_ \\| __| | '_ \\  | | |_ |/ _` | '_ ` _ \\ / _ \\
                | |__| | |__| |  __/ | | | |_| | | | | | |__| | (_| | | | | | |  __/
                 \\___\\_\\\\____/ \\___|_| |_|\\__|_|_| |_|  \\_____|\\__,_|_| |_| |_|\\___|
                by Luis Bolaños Mures

                """);

        System.out.println("Welcome to Quentin!");
        System.out.println("Enter commands (type 'exit' to quit):");

        while (true) {
            System.out.print("QuentinGame > ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "localgame", "lg" -> startLocalGame();
                case "onlinegame", "og" -> startOnlineGame();
                case "sog", "startonlinegui" -> startOnlineGui();
                case "slg", "startlocalgui" -> startLocalGui();
                case "" -> {
                    break;
                }
                case "help" -> showHelper();
                case "exit" -> {
                    System.out.println("Exiting the game...");
                    return;
                }
                default -> System.out.println("Unknown command: " + command);
            }
        }
    }

    public static void showHelper() {
        String[][] commands = {
            {"startlocalgui or slg", "Start a match in local mode with gui"},
            {"startonlinegui or sog", "Start a match on the local network with gui"},
            {"onlinegame or og", "Start a match in the local network with terminal"},
            {"localgame or lg", "Start a match in local mode with terminal"},
            {"exit", "Quits the game and exits the program"},
            {"help", "Shows this help"}
        };
        System.out.println("Available commands:");
        System.out.println("------------------");
        for (String[] strings : commands) {
            System.out.printf("  %-25s:%-40s%n", strings[0], strings[1]);
        }
    }

    public static void startLocalGame() {
        CachedGameStarter gameStarter = new CachedGameStarter(CacheHandler.initialize(CACHE_FILE));
        if (gameStarter.getCache().getMemorySize() > 1) {
            System.out.printf(
                    "Old match found with date %s%n%n Do you want to continue? Type Y or N: ",
                    gameStarter.getCache().getLog().getReadableTimestamp());
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("n") || answer.equals("no")) {
                gameStarter = new CachedGameStarter();
                CacheHandler.clearCache();
            }
        }
        gameStarter.run();
        if (!gameStarter.isGameFinished()) {
            CacheHandler.saveCache(CACHE_FILE, gameStarter.getCache());
        }
    }

    public static void startOnlineGame() {
        NetworkStarter starter = new NetworkStarter();
        starter.run();
    }

    public static void startOnlineGui() {
        Application.launch(OnlineGuiLauncher.class);
    }

    public static void startLocalGui() {
        Application.launch(GuiLauncher.class);
    }
}
