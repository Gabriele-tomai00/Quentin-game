package quentin.network;

import java.util.Scanner;

import quentin.game.BoardPoint;
import quentin.game.GameStarter;

public class OnlineGameStarter extends GameStarter {

  private final NetworkHandler handler;
  private final OnlineGame game;
  private boolean continueGame = true;
  private boolean canPlayPie;

  public OnlineGameStarter(NetworkHandler handler, OnlineGame game) {
    this.handler = handler;
    this.game = game;
    canPlayPie = game.getCurrentPlayer()
                     .color() != BoardPoint.BLACK;
  }

  @Override
  public void run() {
    try (Scanner scanner = new Scanner(System.in)) {
      display();
      while (continueGame) {
        if (game.hasWon(game.getCurrentPlayer())) {
          displayWinner();
          break;
        }
        if (!game.canPlayerPlay()) {
          handler.sendCommands("change");
        } else {
          do {
            displayMessage(String.format("%s > %n", game.getCurrentPlayer()));
          } while (!processInput(scanner.nextLine()));
          display();
          if (game.hasWon(game.getCurrentPlayer())) {
            displayWinner();
            continueGame = false;
          }
        }
      }
    }
  }

  @Override
  public synchronized boolean processInput(String command) {
    return switch (command) {
    case "pie"  -> {
      if (canPlayPie) {
        game.applyPieRule();
        handler.sendCommands(command);
        canPlayPie = false;
      }
      yield false;
    }
    case "exit" -> {
      handler.sendCommands(command);
      yield true;
    }
    default     -> super.processInput(command);
    };
  }

  @Override
  public void makeMove(String position) {
    if (canPlayPie) { canPlayPie = false; }
    super.makeMove(position);
    handler.sendCommands(position);
  }
}
