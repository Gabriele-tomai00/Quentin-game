package quentin.game;

import java.io.Serial;
import java.util.Objects;

public class LocalGame extends Game {

    @Serial private static final long serialVersionUID = -8782140056307981297L;
    private static final Player white = new Player(BoardPoint.WHITE);
    private static final Player black = new Player(BoardPoint.BLACK);
    private Player currentPlayer;

    public LocalGame() {
        super();
        currentPlayer = black;
    }

    public LocalGame(LocalGame game) {
        super();
        getBoard().setBoard(game.getBoard());
        currentPlayer = new Player(game.getCurrentPlayer().color());
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
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Game game) {
            return this.getBoard().equals(game.getBoard())
                    && this.getCurrentPlayer().equals(game.getCurrentPlayer());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), getCurrentPlayer());
    }
}
