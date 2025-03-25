package quentin.gui;

import quentin.game.BoardPoint;
import quentin.game.OnlineGame;

public class OnlineGuiGame extends OnlineGame {

    private static final long serialVersionUID = 5942138526532573138L;
    private GuiBoard board;

    public OnlineGuiGame(BoardPoint color) {
        super(color);
        board = new GuiBoard();
    }

    @Override
    public GuiBoard getBoard() {
        return board;
    }
}
