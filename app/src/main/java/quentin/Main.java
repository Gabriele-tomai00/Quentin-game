package quentin;

import java.util.Scanner;
import quentin.cache.CachedGameStarter;
import quentin.gui.GuiMain;
import quentin.network.OnlineGameParser;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("   ____  _    _            _   _          _____                      ");
        System.out.println(
                "  / __ \\| |  | |          | | (_)        / ____|                     ");
        System.out.println(" | |  | | |  | | ___ _ __ | |_ _ _ __   | |  __  __ _ _ __ ___   ___ ");
        System.out.println(
                " | |  | | |  | |/ _ \\ '_ \\| __| | '_ \\  | | |_ |/ _` | '_ ` _ \\ / _ \\");
        System.out.println(" | |__| | |__| |  __/ | | | |_| | | | | | |__| | (_| | | | | | |  __/");
        System.out.println(
                "  \\___\\_\\\\____/ \\___|_| |_|\\__|_|_| |_|  \\_____|\\__,_|_| |_| |_|\\___|");
        System.out.println(
                " by Luis Bolaños Mures                                                         "
                        + " \n\n");

        System.out.println("Welcome to Quentin!");
        System.out.println("Enter commands (type 'exit' to quit):");

        while (true) {
            System.out.print("main > ");
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
                    startGui(args);
                }
                case "" -> {}
                case "help" -> {
                    showHelper();
                }
                case "exit" -> {
                    System.out.println("Exiting the game...");
                    return;
                }
                default -> {
                    System.out.println("Unknown command: " + command);
                }
            }
        }
    }

    private static void showHelper() {
        System.out.println("Available commands:");
        System.out.println(
                "  startlocalgame or slg   Start a match in local mode (only one computer needed)");
        System.out.println(
                "  onlinegame or og        Start a match in LAN mode (two computer needed)");
        System.out.println("  exit                    Quits the game and exits the program");
        System.out.println("  help                    Shows this help");
    }

    private static void startLocalGame(Scanner scanner) {
        CachedGameStarter starter = new CachedGameStarter();
        starter.start(scanner);
        System.out.println("Returning to main menu...");
    }

    private static void startOnlineGame(Scanner scanner) {
        OnlineGameParser parser = new OnlineGameParser();
        parser.run(scanner);
        System.out.println("Returning to main menu...");
    }

    private static void startGui(String... args) {
        GuiMain.main(args);
        System.out.println("Returning to main menu...");
    }
}
