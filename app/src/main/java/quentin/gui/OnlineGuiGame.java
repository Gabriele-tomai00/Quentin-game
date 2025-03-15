package quentin.gui;

import quentin.game.OnlineGame;
import quentin.game.Player;

public class OnlineGuiGame extends OnlineGame {

    private static final long serialVersionUID = 5942138526532573138L;
    private GuiBoard board;

    public OnlineGuiGame(Player currentPlayer) {
        super(currentPlayer);
        board = new GuiBoard();
    }

    @Override
    public GuiBoard getBoard() {
        return board;
    }
}
