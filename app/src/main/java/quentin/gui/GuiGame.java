package quentin.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import quentin.game.LocalGame;

public class GuiGame extends LocalGame {
    private static final long serialVersionUID = -7843572523653926167L;
    private final transient StringProperty currentPlayerProperty;

    private final GuiBoard board;

    public GuiGame() {
        super();
        board = new GuiBoard();
        currentPlayerProperty = new SimpleStringProperty("Black's turn");
    }

    public GuiGame(LocalGame game) {
        super(game);
        board = new GuiBoard();
        board.setBoard(game.getBoard());
        currentPlayerProperty =
                new SimpleStringProperty(game.getCurrentPlayer().toString() + "s turn");
    }

    @Override
    public void changeCurrentPlayer() {
        super.changeCurrentPlayer();
        currentPlayerProperty.set(getCurrentPlayer().toString() + "'s turn");
    }

    @Override
    public GuiBoard getBoard() {
        return board;
    }

    public StringProperty getCurrentPlayerProperty() {
        return currentPlayerProperty;
    }
}
