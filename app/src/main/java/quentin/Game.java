package quentin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import quentin.exceptions.CellAlreadyTakenException;
import quentin.exceptions.IllegalMoveException;

public class Game {
    private final Board board;
    protected Player currentPlayer;
    private final CacheHandler cacheHandler;
    private Boolean matchInProgress;
    private String timestampOfLastMove;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private int moveCounter = 0;
    private boolean isFirstMove = true;

    public Game() {
        currentPlayer = new Player(BoardPoint.BLACK);
        board = new Board();
        cacheHandler = new CacheHandler();
        matchInProgress = true;
    }

    public Game(BoardPoint color) {
        currentPlayer = new Player(color);
        board = new Board();
        cacheHandler = new CacheHandler();
        matchInProgress = true;
    }

    public boolean hasWon(Player player) {
        if (player.color() == BoardPoint.WHITE) {
            for (Cell cell = new Cell(0, 0);
                    cell.row() < boardSize();
                    cell.setRow(cell.row() + 1)) {
                if (board.getPoint(cell) == BoardPoint.WHITE
                        && findWinnerPath(BoardPoint.WHITE, cell)) {
                    return true;
                }
            }
        } else {
            for (Cell cell = new Cell(0, 0);
                    cell.col() < boardSize();
                    cell.setCol(cell.col() + 1)) {
                if (board.getPoint(cell) == BoardPoint.BLACK
                        && findWinnerPath(BoardPoint.BLACK, cell)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean findWinnerPath(BoardPoint color, Cell startPoint) {
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
                            .filter(c -> board.getPoint(c) == color && !visited.contains(c))
                            .toList();
            toVisit.addAll(neighbors);
            visited.add(visiting);
        }
        return false;
    }

    public void coverTerritories(Cell cell) {
        Set<Cell> neighbors = getNeighbors(cell);
        for (Cell neighbor : neighbors) {
            if (board.getPoint(neighbor) != BoardPoint.EMPTY) {
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
                                    .map(board::getPoint)
                                    .filter(a -> a == BoardPoint.WHITE)
                                    .count();
            int blacks =
                    (int)
                            frontier.stream()
                                    .parallel()
                                    .map(board::getPoint)
                                    .filter(a -> a == BoardPoint.BLACK)
                                    .count();
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
                    BoardPoint color =
                            currentPlayer.color() == BoardPoint.BLACK
                                    ? BoardPoint.WHITE
                                    : BoardPoint.BLACK;
                    board.placeStone(color, tile.row(), tile.col());
                }
            }
        }
    }

    Set<Cell> findTerritories(Cell startingCell) {
        Set<Cell> territory = new HashSet<Cell>();
        Deque<Cell> visiting = new LinkedList<Cell>();
        visiting.add(startingCell);

        while (!visiting.isEmpty()) {
            Cell cell = visiting.pop();
            Set<Cell> neighbors = getNeighbors(cell);
            if (neighbors.stream().map(board::getPoint).filter(a -> a != BoardPoint.EMPTY).count()
                    < 2) {
                return Collections.emptySet();
            }
            territory.add(cell);
            visiting.addAll(
                    neighbors.stream()
                            .parallel()
                            .filter(
                                    a ->
                                            board.getPoint(a) == BoardPoint.EMPTY
                                                    && !territory.contains(a))
                            .toList());
        }
        return territory;
    }

    Set<Cell> getNeighbors(Cell pos) {
        int row = pos.row();
        int col = pos.col();
        Set<Cell> neighbors = new HashSet<Cell>();
        if (row > 0) {
            neighbors.add(new Cell(row - 1, col));
        }
        if (col > 0) {
            neighbors.add(new Cell(row, col - 1));
        }
        if (row < board.size() - 1) {
            neighbors.add(new Cell(row + 1, col));
        }
        if (col < board.size() - 1) {
            neighbors.add(new Cell(row, col + 1));
        }
        return neighbors;
    }

    public boolean isMoveValid(BoardPoint color, Cell cell) {
        if (board.getPoint(cell) != BoardPoint.EMPTY) {
            throw new CellAlreadyTakenException(cell);
        }
        int row = cell.row();
        int col = cell.col();
        if (row > 0 && col > 0 && board.getPoint(new Cell(row - 1, col - 1)) == color) {
            if (board.getPoint(new Cell(row, col - 1)) != color
                    && board.getPoint(new Cell(row - 1, col)) != color) {
                return false;
            }
        }
        if (row > 0
                && col < board.size() - 1
                && board.getPoint(new Cell(row - 1, col + 1)) == color) {
            if (board.getPoint(new Cell(row, col + 1)) != color
                    && board.getPoint(new Cell(row - 1, col)) != color) {
                return false;
            }
        }
        if (row < board.size() - 1
                && col > 0
                && board.getPoint(new Cell(row + 1, col - 1)) == color) {
            if (board.getPoint(new Cell(row, col - 1)) != color
                    && board.getPoint(new Cell(row + 1, col)) != color) {
                return false;
            }
        }
        if (row < board.size() - 1
                && col < board.size() - 1
                && board.getPoint(new Cell(row + 1, col + 1)) == color) {
            if (board.getPoint(new Cell(row, col + 1)) != color
                    && board.getPoint(new Cell(row + 1, col)) != color) {
                return false;
            }
        }
        return true;
    }

    public void place(Cell cell) {
        if (isFirstMove) {
            cacheHandler.saveLog(this);
            isFirstMove = false;
        }

        if (!isMoveValid(currentPlayer.color(), cell)) {
            throw new IllegalMoveException(cell);
        }
        timestampOfLastMove = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        board.placeStone(currentPlayer.color(), cell.row(), cell.col());
        moveCounter++;
        cacheHandler.saveLog(this);
        // System.out.println(
        // "FINE PLEACE: indice dal cache: "
        // + cacheHandler.getMoveIndex()
        // + " moveCounter: "
        // + moveCounter);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int letterToIndex(char letter) {
        // Convert the character to uppercase to handle both cases
        char upperLetter = Character.toUpperCase(letter);
        if (upperLetter < 'A' || upperLetter > 'M') {
            return -1;
        }
        return upperLetter - 'A';
    }

    public Boolean currentPlayerHasWon() {
        if (hasWon(currentPlayer)) {
            matchInProgress = false;
            return true;
        } else return false;
    }

    public void stop() {
        cacheHandler.forceSaveLog();
    }

    public Boolean isInProgress() {
        return matchInProgress;
    }

    public boolean isOldMatch() {
        try {
            cacheHandler.loadLogsInMemoryFromDisk();
            cacheHandler.readLastLog();
        } catch (Exception e) {
            System.out.println("NO Previous match found");
            return false;
        }
        return true;
    }

    public void getOldMatch() {
        BoardLog log = cacheHandler.readLastLog();
        board.fromCompactString(log.board());
        timestampOfLastMove = log.timestamp();
        currentPlayer = new Player(BoardPoint.fromString(log.nextMove()));
        moveCounter = log.moveCounter();
        matchInProgress = true;
        cacheHandler.clearCache();
    }

    public void removeOldMatches() {
        cacheHandler.clearCache();
    }

    public String getTimestampOfLastMove() {
        return timestampOfLastMove;
    }

    public void goBackOneMove() {
        if (cacheHandler.getMoveIndex() <= 0) {
            System.out.println("No previous move found");
            return;
        }
        try {
            cacheHandler.decrementIndex();
            BoardLog log = cacheHandler.readLog(cacheHandler.getMoveIndex());
            board.fromCompactString(log.board());
            currentPlayer = new Player(BoardPoint.fromString(log.nextMove()));
            moveCounter--;
        } catch (Exception e) {
            System.out.println("No previous move found");
        }
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public boolean canPlayerPlay() {
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (isMoveValid(currentPlayer.color(), new Cell(row, col))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }

    public int boardSize() {
        return board.size();
    }
}
