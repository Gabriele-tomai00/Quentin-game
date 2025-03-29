package quentin.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import quentin.game.BoardPoint;
import quentin.game.OnlineGame;

public class OnlineGuiGame extends OnlineGame {

    private static final long serialVersionUID = 5942138526532573138L;
    private GuiBoard board;
    private transient BooleanProperty someoneWon;

    public OnlineGuiGame(BoardPoint color) {
        super(color);
        board = new GuiBoard();
        someoneWon = new SimpleBooleanProperty(false);
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
