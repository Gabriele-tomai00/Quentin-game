package quentin.game;

import java.io.Serial;

public class OnlineGame extends Game {

    @Serial private static final long serialVersionUID = 5269244630137536200L;
    private Player currentPlayer;
    private BoardPoint opponentColor;

    public OnlineGame(BoardPoint color) {
        super();
        this.currentPlayer = new Player(color);
        opponentColor = color == BoardPoint.BLACK ? BoardPoint.WHITE : BoardPoint.BLACK;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void applyPieRule() {
        opponentColor = getCurrentPlayer().color();
        currentPlayer =
                currentPlayer.color() == BoardPoint.BLACK
                        ? new Player(BoardPoint.WHITE)
                        : new Player(BoardPoint.BLACK);
    }

    public void opponentPlaces(Cell cell) {
        place(cell, opponentColor);
    }

    public void opponentCoversTerritories(Cell cell) {
        coverTerritories(cell, opponentColor);
    }

    public BoardPoint getOpponentColor() {
        return opponentColor;
    }
}
