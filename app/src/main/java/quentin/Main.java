package quentin;

import java.io.File;
import java.util.Scanner;
import javafx.application.Application;
import quentin.cache.CacheHandler;
import quentin.game.CachedGameStarter;
import quentin.gui.GuiMain2;
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
            if (!scanner.hasNextLine()) {
                System.out.println("No input available. Please try again.");
                continue;
            }
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "slg", "startlocalgame" -> startLocalGame();
                case "og", "onlinegame" -> startOnlineGame();
                case "sg", "startgui" -> startGui();
                case "" -> {
                    continue;
                }
                case "help" -> {
                    showHelper();
                    continue;
                }
                case "exit" -> System.out.println("Exiting the game...");
                default -> {
                    System.out.println("Unknown command: " + command);
                    continue;
                }
            }
            break;
        }
    }

    public static void showHelper() {
        String[][] commands = {
            {"startlocalgame or slg", "Start a match in local mode (only one computer needed)"},
            {"onlinegame or og", "Start a match in LAN mode (two computer needed)"},
            {"startgui or sg", "Start a match in a graphical interface (only one computer needed)"},
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

    public static void startGui(String... args) {
        Application.launch(GuiMain2.class);
    }
}
