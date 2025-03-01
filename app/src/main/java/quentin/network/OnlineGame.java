package quentin.network;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import quentin.exceptions.MoveException;
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
    private boolean isFirstMove = true;

    public OnlineGame(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        board = new GameBoard();
    }

    public void updateLastMoves(GameBoard newBoard) {
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

    public void updateBoard(GameBoard newBoard) {
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
    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board.setBoard(board);
    }

    public void applyPieRule() {
        currentPlayer =
                currentPlayer.color() == BoardPoint.BLACK
                        ? new Player(BoardPoint.WHITE)
                        : new Player(BoardPoint.BLACK);
    }

    @Override
    public boolean canPlayerPlay() {
        Player otherPlayer =
                BoardPoint.BLACK == currentPlayer.color()
                        ? new Player(BoardPoint.WHITE)
                        : new Player(BoardPoint.BLACK);
        for (int row = 0; row < boardSize(); row++) {
            for (int col = 0; col < boardSize(); col++) {
                try {
                    if (isMoveValid(otherPlayer.color(), new Cell(row, col))) {
                        return true;
                    }
                } catch (MoveException e) {
                    // do nothing
                }
            }
        }
        return false;
    }
}
