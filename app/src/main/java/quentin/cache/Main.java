package quentin.cache;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CachedGameStarter gameStarter = CacheHandler.initialize();
        if (gameStarter.getCache().getMemorySize() > 0) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.printf(
                        "Old match found with date %s%n%n Do you want to continue? Y or N",
                        gameStarter.getCache().getLog().timeStamp());
                String answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("n") || answer.equals("no")) {
                    gameStarter = new CachedGameStarter();
                }
                gameStarter.start();
            }
        }
        if (!gameStarter.isGameFinished()) {
            CacheHandler.saveCache(gameStarter.getCache());
        }
    }
}
