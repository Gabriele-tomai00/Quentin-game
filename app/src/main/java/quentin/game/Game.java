package quentin.game;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import quentin.exceptions.CellAlreadyTakenException;
import quentin.exceptions.IllegalMoveException;

public interface Game {
    public default boolean hasWon(Player player) {
        if (player.color() == BoardPoint.WHITE) {
            for (Cell cell = new Cell(0, 0);
                    cell.row() < boardSize();
                    cell.setRow(cell.row() + 1)) {
                if (getBoard().getPoint(cell) == BoardPoint.WHITE
                        && findWinnerPath(BoardPoint.WHITE, cell)) {
                    return true;
                }
            }
        } else {
            for (Cell cell = new Cell(0, 0);
                    cell.col() < boardSize();
                    cell.setCol(cell.col() + 1)) {
                if (getBoard().getPoint(cell) == BoardPoint.BLACK
                        && findWinnerPath(BoardPoint.BLACK, cell)) {
                    return true;
                }
            }
        }
        return false;
    }

    public default boolean findWinnerPath(BoardPoint color, Cell startPoint) {
        Set<Cell> visited = new HashSet<Cell>();
        Deque<Cell> toVisit = new LinkedList<Cell>();

        toVisit.push(startPoint);

        while (!toVisit.isEmpty()) {
            Cell visiting = toVisit.pop();
            if ((color == BoardPoint.WHITE && visiting.col() == boardSize() - 1)
                    || (color == BoardPoint.BLACK && visiting.row() == boardSize() - 1)) {
                return true;
            }
            List<Cell> neighbors =
                    getNeighbors(visiting).stream()
                            .filter(c -> getBoard().getPoint(c) == color && !visited.contains(c))
                            .toList();
            toVisit.addAll(neighbors);
            visited.add(visiting);
        }
        return false;
    }

    public default void coverTerritories(Cell cell) {
        Set<Cell> neighbors = getNeighbors(cell);
        for (Cell neighbor : neighbors) {
            if (getBoard().getPoint(neighbor) != BoardPoint.EMPTY) {
                continue;
            }
            Set<Cell> territory = findTerritories(neighbor);
            Set<Cell> frontier = new HashSet<Cell>();
            for (Cell tile : territory) {
                frontier.addAll(getNeighbors(tile));
            }
            int whites =
                    (int)
                            frontier.stream()
                                    .parallel()
                                    .map(getBoard()::getPoint)
                                    .filter(a -> a == BoardPoint.WHITE)
                                    .count();
            int blacks =
                    (int)
                            frontier.stream()
                                    .parallel()
                                    .map(getBoard()::getPoint)
                                    .filter(a -> a == BoardPoint.BLACK)
                                    .count();
            if (whites > blacks) {
                for (Cell tile : territory) {
                    getBoard().placeStone(BoardPoint.WHITE, tile.row(), tile.col());
                }
            } else if (whites < blacks) {
                for (Cell tile : territory) {
                    getBoard().placeStone(BoardPoint.BLACK, tile.row(), tile.col());
                }
            } else {
                for (Cell tile : territory) {
                    BoardPoint color =
                            getCurrentPlayer().color() == BoardPoint.BLACK
                                    ? BoardPoint.WHITE
                                    : BoardPoint.BLACK;
                    getBoard().placeStone(color, tile.row(), tile.col());
                }
            }
        }
    }

    public default Set<Cell> findTerritories(Cell startingCell) {
        Set<Cell> territory = new HashSet<Cell>();
        Deque<Cell> visiting = new LinkedList<Cell>();
        visiting.add(startingCell);

        while (!visiting.isEmpty()) {
            Cell cell = visiting.pop();
            Set<Cell> neighbors = getNeighbors(cell);
            if (neighbors.stream()
                            .map(getBoard()::getPoint)
                            .filter(a -> a != BoardPoint.EMPTY)
                            .count()
                    < 2) {
                return Collections.emptySet();
            }
            territory.add(cell);
            visiting.addAll(
                    neighbors.stream()
                            .parallel()
                            .filter(
                                    a ->
                                            getBoard().getPoint(a) == BoardPoint.EMPTY
                                                    && !territory.contains(a))
                            .toList());
        }
        return territory;
    }

    default Set<Cell> getNeighbors(Cell pos) {
        int row = pos.row();
        int col = pos.col();
        Set<Cell> neighbors = new HashSet<Cell>();
        if (row > 0) {
            neighbors.add(new Cell(row - 1, col));
        }
        if (col > 0) {
            neighbors.add(new Cell(row, col - 1));
        }
        if (row < getBoard().size() - 1) {
            neighbors.add(new Cell(row + 1, col));
        }
        if (col < getBoard().size() - 1) {
            neighbors.add(new Cell(row, col + 1));
        }
        return neighbors;
    }

    public default boolean isMoveValid(BoardPoint color, Cell cell) {
        if (getBoard().getPoint(cell) != BoardPoint.EMPTY) {
            throw new CellAlreadyTakenException(cell);
        }
        int row = cell.row();
        int col = cell.col();
        if (row > 0 && col > 0 && getBoard().getPoint(new Cell(row - 1, col - 1)) == color) {
            if (getBoard().getPoint(new Cell(row, col - 1)) != color
                    && getBoard().getPoint(new Cell(row - 1, col)) != color) {
                return false;
            }
        }
        if (row > 0
                && col < getBoard().size() - 1
                && getBoard().getPoint(new Cell(row - 1, col + 1)) == color) {
            if (getBoard().getPoint(new Cell(row, col + 1)) != color
                    && getBoard().getPoint(new Cell(row - 1, col)) != color) {
                return false;
            }
        }
        if (row < getBoard().size() - 1
                && col > 0
                && getBoard().getPoint(new Cell(row + 1, col - 1)) == color) {
            if (getBoard().getPoint(new Cell(row, col - 1)) != color
                    && getBoard().getPoint(new Cell(row + 1, col)) != color) {
                return false;
            }
        }
        if (row < getBoard().size() - 1
                && col < getBoard().size() - 1
                && getBoard().getPoint(new Cell(row + 1, col + 1)) == color) {
            if (getBoard().getPoint(new Cell(row, col + 1)) != color
                    && getBoard().getPoint(new Cell(row + 1, col)) != color) {
                return false;
            }
        }
        return true;
    }

    public default void place(Cell cell) {
        if (!isMoveValid(getCurrentPlayer().color(), cell)) {
            throw new IllegalMoveException(cell);
        }
        getBoard().placeStone(getCurrentPlayer().color(), cell.row(), cell.col());
    }

    public Player getCurrentPlayer();

    public default boolean canPlayerPlay() {
        for (int row = 0; row < boardSize(); row++) {
            for (int col = 0; col < boardSize(); col++) {
                if (isMoveValid(getCurrentPlayer().color(), new Cell(row, col))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Board getBoard();

    public default int boardSize() {
        return getBoard().size();
    }
}
