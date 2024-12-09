package quentin;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Game {

  private final Player white;
  private final Player black;
  private final Board board;
  private Player currentPlayer;

  public Game() {
    white = new Player(BoardPoint.WHITE);
    black = new Player(BoardPoint.BLACK);
    currentPlayer = black;
    board = new Board();
  }

  public void coverTerritories(int row, int col) {
    Set<Position> neighbors = neighbors(new Position(row, col));
    for (Position neigh : neighbors) {
      Set<Position> territory = findTerritories(neigh.row(), neigh.col());
      Set<Position> frontier = new HashSet<Position>();
      for (Position cell : territory) {
        frontier.addAll(neighbors(cell));
      }
      int whites =
          (int) frontier.stream().map(board::getPoint).filter(a -> a == BoardPoint.WHITE).count();
      int blacks =
          (int) frontier.stream().map(board::getPoint).filter(a -> a == BoardPoint.BLACK).count();
      if (whites > blacks) {
        // color all cells
      } else if (whites == blacks) {
        // color all cells

      } else {
        changeCurrentPlayer();
        // color all cells
        changeCurrentPlayer();
      }
    }
  }

  public Set<Position> findTerritories(int i, int j) {
    Set<Position> territory = new HashSet<Position>();
    Deque<Position> visiting = new LinkedList<Position>();
    visiting.add(new Position(i, j));
    while (!visiting.isEmpty()) {
      Position pos = visiting.pop();
      Set<Position> neighbors = neighbors(pos);
      if (neighbors.stream().map(board::getPoint).filter(a -> a != BoardPoint.EMPTY).count() >= 2) {
        territory.add(pos);

      } else {
        return Collections.emptySet();
      }
      visiting.addAll(
          neighbors.stream()
              .filter(a -> board.getPoint(a) == BoardPoint.EMPTY && !territory.contains(a))
              .toList());
    }
    return territory;
  }

  public Set<Position> neighbors(Position pos) {
    int row = pos.row();
    int col = pos.col();
    Set<Position> neighbors = new HashSet<Position>();
    if (row > 0) neighbors.add(new Position(row - 1, col));
    if (col > 0) neighbors.add(new Position(row, col - 1));
    if (row < board.SIZE) neighbors.add(new Position(row + 1, col));
    if (col < board.SIZE) neighbors.add(new Position(row, col + 1));
    return neighbors;
  }

  public boolean canPlay() {
    for (int row = 0; row < board.SIZE; row++) {
      for (int col = 0; col < board.SIZE; col++) {
        if (board.isMoveValid(currentPlayer, row, col, true)) return true;
      }
    }
    return false;
  }

  public void place(int row, int col) {
    if (board.isMoveValid(currentPlayer, row, col, true))
      board.placeStone(currentPlayer.color(), row, col);
    else throw new IllegalArgumentException("Not a valid move");
  }

  public Player getCurrenPlayer() {
    return currentPlayer;
  }

  public void changeCurrentPlayer() {
    if (currentPlayer.color() == BoardPoint.WHITE) currentPlayer = black;
    else currentPlayer = white;
  }

  public Board getBoard() {
    return board;
  }
}
