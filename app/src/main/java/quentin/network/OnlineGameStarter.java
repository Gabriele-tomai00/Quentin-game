package quentin.network;

import quentin.game.BoardPoint;
import quentin.game.GameStarter;

public class OnlineGameStarter extends GameStarter {

  private final NetworkHandler handler;
  private final OnlineGame game;
  private boolean canPlayPie;

  public OnlineGameStarter(NetworkHandler handler, OnlineGame game) {
    this.handler = handler;
    this.game = game;
    canPlayPie = game.getCurrentPlayer()
                     .color() != BoardPoint.BLACK;
  }

  @Override
  public synchronized boolean processInput(String command) {
    return switch (command) {
    case "pie" -> {
      if (canPlayPie) {
        game.applyPieRule();
        handler.sendCommands(command);
        canPlayPie = false;
      } else {
        System.err.println("Cannot apply pie rule!");
      }
      yield false;
    }
    case "exit" -> {
      handler.sendCommands(command);
      setContinueGame(false);
      yield true;
    }
    default -> super.processInput(command);
    };
  }

  @Override
  public void makeMove(String position) {
    if (!handler.isWaiting()) {
      if (canPlayPie) { canPlayPie = false; }
      super.makeMove(position);
      handler.sendCommands(position);
    }
  }

  @Override
  public void processCannotPlay() {
    handler.sendCommands("change");
  }
}
