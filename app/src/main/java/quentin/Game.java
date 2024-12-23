package quentin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import quentin.exceptions.CellAlreadyTakenException;
import quentin.exceptions.IllegalMoveException;

public class Game {

    private final Player white;
    private final Player black;
    public final Board board;
    private Player currentPlayer;
    private final CacheHandler cacheHandler;
    private Boolean matchInProgress;
    private String timestampOfLastMove;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private int moveCounter = 0;
    private boolean isFirstMove = true;

    public Game() {
        white = new Player(BoardPoint.WHITE);
        black = new Player(BoardPoint.BLACK);
        currentPlayer = black;
        board = new Board();
        cacheHandler = new CacheHandler();
        matchInProgress = true;
    }

    public void coverTerritories(Cell cell) {
        Set<Cell> neighbors = getNeighbors(cell);
        for (Cell neigh : neighbors) {
            if (board.getPoint(neigh) != BoardPoint.EMPTY) {
                continue;
            }
            Set<Cell> territory = findTerritories(neigh);
            Set<Cell> frontier = new HashSet<>();
            for (Cell tile : territory) {
                frontier.addAll(getNeighbors(tile));
            }
            int whites =
                    (int)
                            frontier.stream()
                                    .map(board::getPoint)
                                    .filter(a -> a == BoardPoint.WHITE)
                                    .count();
            int blacks =
                    (int)
                            frontier.stream()
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
                    changeCurrentPlayer();
                    board.placeStone(currentPlayer.color(), tile.row(), tile.col());
                    changeCurrentPlayer();
                }
            }
        }
    }

    public Set<Cell> findTerritories(Cell cell) {
        Set<Cell> territory = new HashSet<>();
        Deque<Cell> visiting = new LinkedList<>();
        visiting.add(cell);
        while (!visiting.isEmpty()) {
            Cell tile = visiting.pop();
            Set<Cell> neighbors = getNeighbors(tile);
            if (neighbors.stream().map(board::getPoint).filter(a -> a != BoardPoint.EMPTY).count()
                    < 2) {
                return Collections.emptySet();
            }
            territory.add(tile);
            visiting.addAll(
                    neighbors.stream()
                            .filter(
                                    a ->
                                            board.getPoint(a) == BoardPoint.EMPTY
                                                    && !territory.contains(a))
                            .toList());
        }
        return territory;
    }

    public Set<Cell> getNeighbors(Cell pos) {
        int row = pos.row();
        int col = pos.col();
        Set<Cell> neighbors = new HashSet<>();
        if (row > 0) neighbors.add(new Cell(row - 1, col));
        if (col > 0) neighbors.add(new Cell(row, col - 1));
        if (row < board.size() - 1) neighbors.add(new Cell(row + 1, col));
        if (col < board.size() - 1) neighbors.add(new Cell(row, col + 1));
        return neighbors;
    }

    public boolean isValid(BoardPoint color, Cell cell) throws CellAlreadyTakenException {
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
            return board.getPoint(new Cell(row, col + 1)) == color
                    || board.getPoint(new Cell(row + 1, col)) == color;
        }
        return true;
    }

    public boolean canPlayerPlay() {
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                try {
                    if (isValid(currentPlayer.color(), new Cell(row, col))) {
                        return true;
                    }
                } catch (CellAlreadyTakenException e) {
                    // do nothing
                }
            }
        }
        return false;
    }

    public void place(Cell cell) throws MoveException {
        if (isFirstMove) {
            cacheHandler.saveLog(this);
            isFirstMove = false;
        }

        if (!isValid(currentPlayer.color(), cell)) {
            System.out.println("Invalid move");
            throw new IllegalMoveException(
                    String.format(
                            "Cell %s is not connected orthogonally one or more diagonally connected"
                                    + " cells of the same color",
                            cell));
        }
        timestampOfLastMove = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        board.placeStone(currentPlayer.color(), cell.row(), cell.col());
        moveCounter++;
        cacheHandler.saveLog(this);
        //        System.out.println(
        //                "FINE PLEACE: indice dal cache: "
        //                        + cacheHandler.getMoveIndex()
        //                        + " moveCounter: "
        //                        + moveCounter);
    }

    public Player getCurrentPlayer() {
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

    public int letterToIndex(char letter) {
        // Convert the character to uppercase to handle both cases
        char upperLetter = Character.toUpperCase(letter);
        if (upperLetter < 'A' || upperLetter > 'M') {
            return -1;
        }
        return upperLetter - 'A';
    }

    public Boolean currentPlayerHasWon() {
        if (board.hasWon(currentPlayer.color())) {
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
}
