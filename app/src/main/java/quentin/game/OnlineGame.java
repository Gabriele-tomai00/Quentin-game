package quentin.game;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class OnlineGame extends Game {

    @Serial private static final long serialVersionUID = 5269244630137536200L;
    private Player currentPlayer;
    private final List<Cell> lastMoves = new ArrayList<>();

    public OnlineGame(Player currentPlayer) {
        super();
        this.currentPlayer = currentPlayer;
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

    public List<Cell> getLastMoves() {
        return lastMoves;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void applyPieRule() {
        currentPlayer =
                currentPlayer.color() == BoardPoint.BLACK
                        ? new Player(BoardPoint.WHITE)
                        : new Player(BoardPoint.BLACK);
    }
}
