package quentin.game;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class LocalGame implements Game {

    @Serial private static final long serialVersionUID = -8782140056307981297L;
    private static final Player white = new Player(BoardPoint.WHITE);
    private static final Player black = new Player(BoardPoint.BLACK);
    private Player currentPlayer;
    private Board board;
    private final List<Cell> lastMoves =
            new ArrayList<>(); // moves can be two if the opponent player can't play

    public LocalGame() {
        currentPlayer = black;
        board = new Board();
    }

    public LocalGame(LocalGame game) {
        this.board = new Board();
        board.setBoard(game.getBoard());
        this.currentPlayer = new Player(game.getCurrentPlayer().color());
    }

    public void changeCurrentPlayer() {
        if (getCurrentPlayer().color() == black.color()) {
            currentPlayer = white;
        } else {
            currentPlayer = black;
        }
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    public void updateLastMoves(Board newBoard) {
        lastMoves.clear();
        BoardPoint[][] points = newBoard.getBoard();
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (points[row][col] != board.getBoard()[row][col]) {
                    lastMoves.add(new Cell(col, row)); // swap because of the parser
                }
            }
        }
    }

    public void updateBoard(Board newBoard) {
        updateLastMoves(newBoard);
        board = newBoard;
    }

    public List<Cell> getLastMoves() {
        return lastMoves;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Game game) {
            if (this.getBoard().equals(game.getBoard())
                    && this.getCurrentPlayer().equals(game.getCurrentPlayer())) {
                return true;
            }
        }
        return false;
    }
}
