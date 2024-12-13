package quentin;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    final String CLEAR = "\033[H\033[2J";
    Game game = new Game();

    System.out.println("   ____  _    _            _   _          _____                      ");
    System.out.println("  / __ \\| |  | |          | | (_)        / ____|                     ");
    System.out.println(" | |  | | |  | | ___ _ __ | |_ _ _ __   | |  __  __ _ _ __ ___   ___ ");
    System.out.println(" | |  | | |  | |/ _ \\ '_ \\| __| | '_ \\  | | |_ |/ _` | '_ ` _ \\ / _ \\");
    System.out.println(" | |__| | |__| |  __/ | | | |_| | | | | | |__| | (_| | | | | | |  __/");
    System.out.println("  \\___\\_\\\\____/ \\___|_| |_|\\__|_|_| |_|  \\_____|\\__,_|_| |_| |_|\\___|");
    System.out.println(" by Luis Bolaños Mures                                                          ");

    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        if (game.canPlayerPlay()) {
          System.out.printf("%s player turn:", game.getCurrenPlayer());
          while (true) {
            try {
              Cell cell = new Parser(scanner.next()).parse();
              game.place(cell);
              game.coverTerritories(cell);
              break;
            } catch (Exception e) {
              System.out.println("Invalid input!");
            }
          }
          System.out.print(CLEAR);
          System.out.println(game.getBoard());
        }
        if (game.getBoard()
                .hasWon(game.getCurrenPlayer()
                            .color())) {
          System.out.print(CLEAR);
          System.out.printf("%s has won", game.getCurrenPlayer());
          break;
        }
        game.changeCurrentPlayer();
        if (game.getBoard()
                .hasWon(game.getCurrenPlayer()
                            .color())) {
          System.out.print(CLEAR);
          System.out.printf("%s has won", game.getCurrenPlayer());
          break;
        }
      }
    }
  }
}
