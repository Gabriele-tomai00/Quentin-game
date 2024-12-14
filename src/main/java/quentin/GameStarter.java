package quentin;

public class GameStarter {
  private Game game;
  private Displayer displayer;
  private InputGetter inputter;

  public GameStarter() {
    game = new Game();

  }

  public void start() {

    displayer.display();// System.out.println(game.getBoard());

    while (true) {
      if (game.canPlayerPlay()) {
        displayer.displayMessage(String.format("%s turn:", game.getCurrentPlayer()));
        while (true) {
          try {
            Cell cell = inputter.getInput();
            game.place(cell);
            game.coverTerritories(cell);
            break;
          } catch (MoveException e) {
            System.out.println(e);
          }
        }
        displayer.display();// System.out.println(game.getBoard());
      }
      if (game.getBoard()
              .hasWon(game.getCurrentPlayer()
                          .color())) {
        displayer.displayWinner();
        // System.out.printf("%s has won", game.getCurrentPlayer());
        break;
      }
      game.changeCurrentPlayer();
      if (game.getBoard()
              .hasWon(game.getCurrentPlayer()
                          .color())) {
        displayer.displayWinner();
        // System.out.printf("%s has won", game.getCurrentPlayer());
        break;
      }
    }
  }

  public void place() {

  }
}
