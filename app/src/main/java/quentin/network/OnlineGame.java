package quentin.network;

import java.util.ArrayList;
import java.util.List;
import quentin.game.Board;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.Game;
import quentin.game.Player;

public class OnlineGame implements Game {

    private static final long serialVersionUID = 5269244630137536200L;
    private Player currentPlayer;
    private Board board;
    private final List<Cell> lastMoves =
            new ArrayList<>(); // moves can be two if the opponent player can't play
    private boolean isFirstMove = true;

    public OnlineGame() {
        currentPlayer = new Player(BoardPoint.BLACK);
        board = new Board();
    }

    public OnlineGame(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        board = new Board();
    }

    public OnlineGame(OnlineGame game) {
        this.board = new Board();
        board.setBoard(game.getBoard());
        this.currentPlayer = new Player(game.getCurrentPlayer().color());
        this.isFirstMove = game.isFirstMove();
    }

    public void updateLastMoves(Board newBoard) {
        lastMoves.clear();
        BoardPoint[][] points = newBoard.getBoard();
        for (int row = 0; row < getBoard().size(); row++) {
            for (int col = 0; col < getBoard().size(); col++) {
                if (points[row][col] != getBoard().getBoard()[row][col]) {
                    lastMoves.add(new Cell(row, col));
                }
            }
        }
    }

    public void updateBoard(Board newBoard) {
        updateLastMoves(newBoard);
        setBoard(newBoard);
    }

    public List<Cell> getLastMoves() {
        return lastMoves;
    }

    public boolean isFirstMove() { // white always plays for second
        return isFirstMove;
    }

    public void setFirstMove(boolean firstM) { // white always plays for second
        isFirstMove = firstM;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board.setBoard(board);
    }

    public void applyPieRule() {
        currentPlayer =
                currentPlayer.color() == BoardPoint.BLACK
                        ? new Player(BoardPoint.WHITE)
                        : new Player(BoardPoint.BLACK);
    }
}
