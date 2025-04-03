package quentin.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OnlineGameTest {

    @Test
    void pieTest() {
        OnlineGame game = new OnlineGame(BoardPoint.WHITE);
        game.applyPieRule();
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        assertEquals(BoardPoint.WHITE, game.getOpponentColor());
    }
}
