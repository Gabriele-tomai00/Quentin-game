package quentin;

import java.util.Scanner;
import quentin.cache.CachedGameStarter;
import quentin.gui.GuiMain;
import quentin.network.OnlineGameStarter;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

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
                    case "slg", "startlocalgame" -> {
                        startLocalGame(scanner);
                    }
                    case "og", "onlinegame" -> {
                        startOnlineGame(scanner);
                    }
                    case "sg", "startgui" -> {
                        startGui();
                    }
                    case "" -> {
                        continue;
                    }
                    case "help" -> {
                        showHelper();
                        continue;
                    }
                    case "exit" -> {
                        System.out.println("Exiting the game...");
                    }
                    default -> {
                        System.out.println("Unknown command: " + command);
                        continue;
                    }
                }
                break;
            }
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

    public static void startLocalGame(Scanner scanner) {
        CachedGameStarter starter = new CachedGameStarter();
        starter.start();
    }

    public static void startOnlineGame(Scanner scanner) {
        OnlineGameStarter parser = new OnlineGameStarter();
        parser.run(scanner);
    }

    public static void startGui() {
        GuiMain.main();
    }
}
