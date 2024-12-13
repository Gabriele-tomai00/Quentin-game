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

  public void coverTerritories(Cell cell) {
    Set<Cell> neighbors = getNeighbors(cell);
    for (Cell neigh : neighbors) {
      Set<Cell> territory = findTerritories(neigh);
      Set<Cell> frontier = new HashSet<Cell>();
      for (Cell tile : territory) {
        frontier.addAll(getNeighbors(tile));
      }
      int whites =
          (int) frontier.stream().map(board::getPoint).filter(a -> a == BoardPoint.WHITE).count();
      int blacks =
          (int) frontier.stream().map(board::getPoint).filter(a -> a == BoardPoint.BLACK).count();
      if (whites > blacks) {
        for (Cell tile : territory) {
          board.placeStone(BoardPoint.WHITE, tile.row(), tile.col());
        }
      } else if (whites < blacks) {
        for (Cell tile : territory) {
          board.placeStone(BoardPoint.BLACK, tile.row(), tile.col());
        }
      } else {
        for (Cell tile : territory) {
          changeCurrentPlayer();
          board.placeStone(currentPlayer.color(), tile.row(), tile.col());
          changeCurrentPlayer();
        }
      }
    }
  }

  public Set<Cell> findTerritories(Cell cell) {
    Set<Cell> territory = new HashSet<Cell>();
    Deque<Cell> visiting = new LinkedList<Cell>();
    visiting.add(cell);
    while (!visiting.isEmpty()) {
      Cell tile = visiting.pop();
      Set<Cell> neighbors = getNeighbors(tile);
      if (neighbors.stream().map(board::getPoint).filter(a -> a != BoardPoint.EMPTY).count() < 2) {
        return Collections.emptySet();
      }
      territory.add(tile);
      visiting.addAll(
          neighbors.stream()
              .filter(a -> board.getPoint(a) == BoardPoint.EMPTY && !territory.contains(a))
              .toList());
    }
    return territory;
  }

  public Set<Cell> getNeighbors(Cell pos) {
    int row = pos.row();
    int col = pos.col();
    Set<Cell> neighbors = new HashSet<Cell>();
    if (row > 0) neighbors.add(new Cell(row - 1, col));
    if (col > 0) neighbors.add(new Cell(row, col - 1));
    if (row < board.size() - 1) neighbors.add(new Cell(row + 1, col));
    if (col < board.size() - 1) neighbors.add(new Cell(row, col + 1));
    return neighbors;
  }

  public boolean isValid(Cell cell) {
    if (board.getPoint(cell) != BoardPoint.EMPTY) {
      return false;
    } // throw new
    // CellAlreadyTakenException();
    int row = cell.row();
    int col = cell.col();
    if (row > 0 && col > 0 && board.getPoint(new Cell(row - 1, col - 1)) == currentPlayer.color()) {
      if (board.getPoint(new Cell(row, col - 1)) != currentPlayer.color()
          && board.getPoint(new Cell(row - 1, col)) != currentPlayer.color()) {
        return false;
      }
    }
    if (row > 0
        && col < board.size() - 1
        && board.getPoint(new Cell(row - 1, col + 1)) == currentPlayer.color()) {
      if (board.getPoint(new Cell(row, col + 1)) != currentPlayer.color()
          && board.getPoint(new Cell(row - 1, col)) != currentPlayer.color()) {
        return false;
      }
    }
    if (row < board.size() - 1
        && col > 0
        && board.getPoint(new Cell(row + 1, col - 1)) == currentPlayer.color()) {
      if (board.getPoint(new Cell(row, col - 1)) != currentPlayer.color()
          && board.getPoint(new Cell(row + 1, col)) != currentPlayer.color()) {
        return false;
      }
    }
    if (row < board.size() - 1
        && col < board.size() - 1
        && board.getPoint(new Cell(row + 1, col + 1)) == currentPlayer.color()) {
      if (board.getPoint(new Cell(row, col + 1)) != currentPlayer.color()
          && board.getPoint(new Cell(row + 1, col)) != currentPlayer.color()) {
        return false;
      }
    }
    return true;
  }

  public boolean canPlayerPlay() {
    for (int row = 0; row < board.size(); row++) {
      for (int col = 0; col < board.size(); col++) {
        if (isValid(new Cell(row, col))) {
          return true;
        }
      }
    }
    return false;
  }

  public void place(Cell cell) {
    if (!isValid(cell)) {
      throw new IllegalArgumentException("Not a valid move");
    }
    board.placeStone(currentPlayer.color(), cell.row(), cell.col());
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

  public int boardSize() {
    return board.size();
  }
}
