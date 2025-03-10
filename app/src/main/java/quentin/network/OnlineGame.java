package quentin.network;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.Game;
import quentin.game.GameBoard;
import quentin.game.Player;

public class OnlineGame implements Game {

  @Serial private static final long serialVersionUID = 5269244630137536200L;
  private Player currentPlayer;
  private final GameBoard board;
  private final List<Cell> lastMoves = new ArrayList<>();

  public OnlineGame(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
    board = new GameBoard();
  }

  public void updateLastMoves(GameBoard newBoard) {
    lastMoves.clear();
    BoardPoint[][] points = newBoard.getBoard();
    for (int row = 0; row < getBoard().size(); row++) {
      for (int col = 0; col < getBoard().size(); col++) {
        if (points[row][col] != getBoard().getBoard()[row][col]) { lastMoves.add(new Cell(row, col)); }
      }
    }
  }

  public void updateBoard(GameBoard newBoard) {
    updateLastMoves(newBoard);
    setBoard(newBoard);
  }

  public List<Cell> getLastMoves() {
    return lastMoves;
  }

  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public GameBoard getBoard() {
    return board;
  }

  public void setBoard(GameBoard board) {
    this.board.setBoard(board);
  }

  public void applyPieRule() {
    currentPlayer = currentPlayer.color() == BoardPoint.BLACK ? new Player(BoardPoint.WHITE)
        : new Player(BoardPoint.BLACK);
  }
}
