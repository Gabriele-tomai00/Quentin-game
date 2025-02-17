package quentin.game;

import java.util.Scanner;

public class SimpleGameStarter implements GameStarter {

  protected LocalGame game;
  private Scanner scanner;

  public SimpleGameStarter() {
    game = new LocalGame();
    scanner = new Scanner(System.in);
  }

  @Override
  public void start() {
    display();
    while (true) {
      if (!game.canPlayerPlay()) {
        game.changeCurrentPlayer();
      } else {
        if (processInput(scanner)) { break; }
        display();
      }
    }
  }

  public boolean processInput(Scanner scanner) {
    while (true) {
      displayMessage(String.format("%s > ", game.getCurrentPlayer()));
      try {
        makeMove(scanner.nextLine());
        break;
      } catch (RuntimeException e) {
        displayMessage(e.getMessage());
      }
    }
    return hasWon();
  }

  public boolean hasWon() {
    if (game.hasWon(game.getCurrentPlayer())) {
      displayWinner();
      return true;
    }
    game.changeCurrentPlayer();
    if (game.hasWon(game.getCurrentPlayer())) {
      displayWinner();
      return true;
    }
    return false;
  }

  public void makeMove(String input) {
    Cell cell = new MoveParser(input).parse();
    game.place(cell);
    game.coverTerritories(cell);
  }

  @Override
  public Game getGame() {
    return game;
  }
}
