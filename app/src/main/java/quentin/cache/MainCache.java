package quentin.cache;

import java.util.Scanner;

public class MainCache {

    public static void main(String[] args) {
        CachedGameStarter gameStarter = new CachedGameStarter(CacheHandler.initialize());
        try (Scanner scanner = new Scanner(System.in)) {
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
            gameStarter.start();
        }
        if (!gameStarter.isGameFinished()) {
            CacheHandler.saveCache(gameStarter.getCache());
        }
    }
}
