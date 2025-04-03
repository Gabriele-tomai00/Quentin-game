package quentin.gui;

import java.io.Serial;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import quentin.game.BoardPoint;
import quentin.game.OnlineGame;

public class OnlineGuiGame extends OnlineGame {

    @Serial private static final long serialVersionUID = 5942138526532573138L;
    private final GuiBoard board;
    private final transient BooleanProperty someoneWon;
    private final transient StringProperty playerColor;
    private transient String winnerMessage;

    public OnlineGuiGame(BoardPoint color) {
        super(color);
        board = new GuiBoard();
        someoneWon = new SimpleBooleanProperty(false);
        playerColor = new SimpleStringProperty(getCurrentPlayer() + " player!!!");
    }

    public StringProperty getPlayerColor() {
        return playerColor;
    }

    @Override
    public void applyPieRule() {
        super.applyPieRule();
        Platform.runLater(() -> playerColor.set(getCurrentPlayer() + " player!!!!"));
    }

    @Override
    public GuiBoard getBoard() {
        return board;
    }

    public void setSomeoneWon(String message) {
        winnerMessage = "YOU " + message + "!!!!";
        Platform.runLater(() -> someoneWon.set(true));
    }

    public BooleanProperty getSomeoneWon() {
        return someoneWon;
    }

    public String getWinnerMessage() {
        return winnerMessage;
    }

    @Override
    public boolean hasWon(BoardPoint color) {
        if (super.hasWon(color)) {
            setSomeoneWon(color == getCurrentPlayer().color() ? "won" : "lost");
            return true;
        }
        return false;
    }
}
