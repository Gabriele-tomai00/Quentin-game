package quentin.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import quentin.exceptions.CellAlreadyTakenException;
import quentin.exceptions.IllegalMoveException;
import quentin.exceptions.MoveException;

public abstract class Game implements Serializable {

    private static final long serialVersionUID = 2946018924798852043L;
    private Board board;

    protected Game() {
        board = new Board();
    }

    public abstract Player getCurrentPlayer();

    public boolean hasWon() {
        return hasWon(getCurrentPlayer().color());
    }

    public boolean hasWon(BoardPoint color) {
        if (color == BoardPoint.WHITE) {
            for (int row = 0; row < boardSize(); row++) {
                Cell cell = new Cell(row, 0);
                if (getBoard().getPoint(cell) == BoardPoint.WHITE
                        && findWinnerPath(BoardPoint.WHITE, cell)) {
                    return true;
                }
            }
        } else {
            for (int col = 0; col < boardSize(); col++) {
                Cell cell = new Cell(0, col);
                if (getBoard().getPoint(cell) == BoardPoint.BLACK
                        && findWinnerPath(BoardPoint.BLACK, cell)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean findWinnerPath(BoardPoint color, Cell startPoint) {
        Set<Cell> visited = new HashSet<>();
        Deque<Cell> toVisit = new LinkedList<>();

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

    public void coverTerritories(Cell cell) {
        coverTerritories(cell, getCurrentPlayer().color());
    }

    public void coverTerritories(Cell cell, BoardPoint color) {
        Set<Cell> neighbors = getNeighbors(cell);
        for (Cell neighbor : neighbors) {
            if (getBoard().getPoint(neighbor) != BoardPoint.EMPTY) {
                continue;
            }
            Set<Cell> territory = findTerritories(neighbor);
            Set<Cell> frontier = new HashSet<>();
            for (Cell tile : territory) {
                frontier.addAll(getNeighbors(tile));
            }
            int whites = countCells(frontier, BoardPoint.WHITE);
            int blacks = countCells(frontier, BoardPoint.BLACK);
            if (whites > blacks) {
                territory.forEach(a -> getBoard().placeStone(BoardPoint.WHITE, a.row(), a.col()));
            } else if (whites < blacks) {
                territory.forEach(a -> getBoard().placeStone(BoardPoint.BLACK, a.row(), a.col()));
            } else {
                BoardPoint otherColor =
                        color == BoardPoint.BLACK ? BoardPoint.WHITE : BoardPoint.BLACK;
                territory.forEach(a -> getBoard().placeStone(otherColor, a.row(), a.col()));
            }
        }
    }

    public int countCells(Set<Cell> frontier, BoardPoint color) {
        return (int) frontier.stream().map(getBoard()::getPoint).filter(a -> a == color).count();
    }

    public Set<Cell> findTerritories(Cell startingCell) {
        Set<Cell> territory = new HashSet<>();
        Deque<Cell> visiting = new LinkedList<>();
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
                            .filter(
                                    a ->
                                            getBoard().getPoint(a) == BoardPoint.EMPTY
                                                    && !territory.contains(a))
                            .toList());
        }
        return territory;
    }

    public Set<Cell> getNeighbors(Cell pos) {
        int row = pos.row();
        int col = pos.col();
        Set<Cell> neighbors = new HashSet<>();
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

    public boolean isMoveValid(BoardPoint color, Cell cell) {
        if (getBoard().getPoint(cell) != BoardPoint.EMPTY) {
            throw new CellAlreadyTakenException(cell);
        }
        int row = cell.row();
        int col = cell.col();
        if (row > 0
                && col > 0
                && getBoard().getPoint(new Cell(row - 1, col - 1)) == color
                && getBoard().getPoint(new Cell(row, col - 1)) != color
                && getBoard().getPoint(new Cell(row - 1, col)) != color) {
            return false;
        }

        if ((row > 0)
                && (col < (getBoard().size() - 1))
                && (getBoard().getPoint(new Cell(row - 1, col + 1)) == color)
                && getBoard().getPoint(new Cell(row, col + 1)) != color
                && getBoard().getPoint(new Cell(row - 1, col)) != color) {
            return false;
        }

        if (row < getBoard().size() - 1
                && col > 0
                && getBoard().getPoint(new Cell(row + 1, col - 1)) == color
                && getBoard().getPoint(new Cell(row, col - 1)) != color
                && getBoard().getPoint(new Cell(row + 1, col)) != color) {
            return false;
        }

        if (row < getBoard().size() - 1
                && col < getBoard().size() - 1
                && getBoard().getPoint(new Cell(row + 1, col + 1)) == color) {
            return getBoard().getPoint(new Cell(row, col + 1)) == color
                    || getBoard().getPoint(new Cell(row + 1, col)) == color;
        }
        return true;
    }

    public void place(Cell cell) {
        place(cell, getCurrentPlayer().color());
    }

    public void place(Cell cell, BoardPoint color) {
        if (!isMoveValid(color, cell)) {
            throw new IllegalMoveException(cell);
        }
        getBoard().placeStone(color, cell.row(), cell.col());
    }

    public boolean canPlayerPlay() {
        for (int row = 0; row < boardSize(); row++) {
            for (int col = 0; col < boardSize(); col++) {
                try {
                    if (isMoveValid(getCurrentPlayer().color(), new Cell(row, col))) {
                        return true;
                    }
                } catch (MoveException e) {
                    // do nothing
                }
            }
        }
        return false;
    }

    public int boardSize() {
        return getBoard().size();
    }

    public Board getBoard() {
        return board;
    }
}
