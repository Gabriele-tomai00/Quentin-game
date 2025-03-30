package quentin.gui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import quentin.game.BoardPoint;
import quentin.game.OnlineGame;

public class OnlineGuiGame extends OnlineGame {

    private static final long serialVersionUID = 5942138526532573138L;
    private GuiBoard board;
    private transient BooleanProperty someoneWon;
    private transient StringProperty playerColor;

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

    public void setSomeoneWon() {
        someoneWon.set(true);
    }

    public BooleanProperty getSomeoneWon() {
        return someoneWon;
    }
}
