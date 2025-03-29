package quentin.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HasWonTest {

    LocalGame game = new LocalGame();

    @Test
    void hasWonException() {
        assertFalse(game.hasWon(BoardPoint.BLACK));
    }

    @Test
    void findWhiteLine() {
        game.changeCurrentPlayer();
        assertEquals(BoardPoint.WHITE, game.getCurrentPlayer().color());

        for (int col = 0; col < game.boardSize(); col++) {
            Cell cell = new Cell(0, col);
            game.place(cell);
        }
        assertTrue(game.hasWon(BoardPoint.WHITE));
    }

    @Test
    void findWhite2Lines() {
        game.changeCurrentPlayer();
        for (int col = 0; col < game.boardSize(); col++) {
            Cell first = new Cell(3, col);
            Cell second = new Cell(6, col);
            game.place(second);
            game.place(first);
        }
        assertEquals(BoardPoint.WHITE, game.getCurrentPlayer().color());
        assertTrue(game.findWinnerPath(BoardPoint.WHITE, new Cell(4, 0)));
        assertTrue(game.findWinnerPath(BoardPoint.WHITE, new Cell(6, 0)));
    }

    @Test
    void findWhiteOrthogonalDownLines() {
        game.changeCurrentPlayer();
        assertEquals(BoardPoint.WHITE, game.getCurrentPlayer().color());
        for (int col = 0; col < 7; col++) {
            Cell cell = new Cell(4, col);
            game.place(cell);
        }
        game.place(new Cell(5, 6));
        game.place(new Cell(6, 6));
        for (int col = 6; col < game.boardSize(); col++) {
            Cell cell = new Cell(7, col);
            game.place(cell);
        }
        assertTrue(game.hasWon(BoardPoint.WHITE));
    }

    @Test
    void findWhiteLinesWithLoops() {
        game.changeCurrentPlayer();
        assertEquals(BoardPoint.WHITE, game.getCurrentPlayer().color());
        for (int i = 0; i < 9; i++) {
            game.place(new Cell(4, i));
        }
        for (int i = 6; i < game.boardSize(); i++) {
            game.place(new Cell(2, i));
        }
        game.place(new Cell(3, 6));
        game.place(new Cell(3, 8));
        assertTrue(game.hasWon(BoardPoint.WHITE));
    }

    @Test
    void findWhiteDifficultPath() {
        game.changeCurrentPlayer();
        assertEquals(BoardPoint.WHITE, game.getCurrentPlayer().color());
        for (int j = 1; j < 9; j++) {
            game.place(new Cell(0, j));
            game.place(new Cell(4, j));
            game.place(new Cell(7, j));
        }
        game.place(new Cell(0, 0));
        game.place(new Cell(4, 0));
        for (int j = 1; j < game.boardSize(); j++) {
            game.place(new Cell(game.boardSize() - 1, j));
        }
        for (int i = 0; i < 8; i++) {
            if (i == 0 || i == 4 || i == 7) {
                continue;
            }
            game.place(new Cell(i, 8));
        }
        for (int i = 8; i < game.boardSize() - 1; i++) {
            game.place(new Cell(i, 1));
        }
        for (int j = 6; j < game.boardSize() - 1; j++) {
            if (j == 8) {
                continue;
            }
            game.place(new Cell(2, j));
        }
        game.place(new Cell(3, 6));
        assertTrue(game.findWinnerPath(game.getCurrentPlayer().color(), new Cell(0, 0)));
        assertTrue(game.findWinnerPath(game.getCurrentPlayer().color(), new Cell(4, 0)));
        assertTrue(game.hasWon(BoardPoint.WHITE));
    }

    @Test
    void findWhitePathInNormalSituation() {
        for (int i = 1; i < 4; i++) {
            game.place(new Cell(i, 4));
        }
        for (int row = 8; row < 11; row++) {
            Cell cell = new Cell(row, 3);
            game.place(cell);
        }
        game.place(new Cell(4, 9));
        game.place(new Cell(5, 9));
        game.place(new Cell(2, game.boardSize() - 1));
        game.place(new Cell(7, 10));
        game.changeCurrentPlayer();
        for (int col = 1; col < 9; col++) {
            game.place(new Cell(0, col));
            game.place(new Cell(4, col));
            game.place(new Cell(7, col));
        }
        game.place(new Cell(0, 0));
        game.place(new Cell(4, 0));
        for (int col = 1; col < game.boardSize(); col++) {
            game.place(new Cell(game.boardSize() - 1, col));
        }
        for (int row = 0; row < 8; row++) {
            if (row == 0 || row == 4 || row == 7) {
                continue;
            }
            game.place(new Cell(row, 8));
        }
        for (int i = 8; i < game.boardSize() - 1; i++) {
            game.place(new Cell(i, 1));
        }
        for (int j = 6; j < game.boardSize() - 1; j++) {
            if (j == 8) {
                continue;
            }
            game.place(new Cell(2, j));
        }
        game.place(new Cell(3, 6));

        assertEquals(BoardPoint.WHITE, game.getCurrentPlayer().color());
        assertTrue(game.hasWon(BoardPoint.WHITE));
    }

    @Test
    void findBlackLine() {
        for (int row = 0; row < game.boardSize(); row++) {
            Cell cell = new Cell(row, 4);
            game.place(cell);
        }
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        assertTrue(game.hasWon(BoardPoint.BLACK));
    }

    @Test
    void findBlack2Lines() {
        for (int row = 0; row < game.boardSize(); row++) {
            Cell first = new Cell(row, 3);
            Cell second = new Cell(row, 6);
            game.place(second);
            game.place(first);
        }
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        assertTrue(game.findWinnerPath(BoardPoint.BLACK, new Cell(0, 2)));
        assertTrue(game.findWinnerPath(BoardPoint.BLACK, new Cell(0, 4)));
    }

    @Test
    void findDarkOrthogonalLeftLines() {
        for (int row = 0; row < 6; row++) {
            Cell cell = new Cell(row, 4);
            game.place(cell);
        }
        game.place(new Cell(5, 3));
        game.place(new Cell(5, 2));
        for (int row = 5; row < game.boardSize(); row++) {
            Cell cell = new Cell(row, 1);
            game.place(cell);
        }
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        assertTrue(game.hasWon(BoardPoint.BLACK));
    }

    @Test
    void findDarkOrthogonalRightLines() {
        for (int row = 0; row < 6; row++) {
            Cell cell = new Cell(row, 2);
            game.place(cell);
        }
        game.place(new Cell(5, 3));
        game.place(new Cell(5, 4));
        for (int row = 5; row < game.boardSize(); row++) {
            Cell cell = new Cell(row, 5);
            game.place(cell);
        }
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        assertTrue(game.hasWon(BoardPoint.BLACK));
    }

    @Test
    void findDarkOrthogonalDifficultLines() {
        for (int i = 0; i < 10; i++) {
            if (i == 6) {
                continue;
            }
            game.place(new Cell(i, 0));
            game.place(new Cell(i, 2));
        }
        for (int col = 1; col < game.boardSize(); col++) {
            Cell cell = new Cell(5, col);
            if (col == 2) {
                continue;
            }
            game.place(cell);
        }
        for (int row = 3; row < 8; row++) {
            if (row == 5) {
                continue;
            }
            game.place(new Cell(row, 4));
        }
        for (int row = 6; row < 10; row++) {
            game.place(new Cell(row, 7));
            game.place(new Cell(row, 9));
        }
        game.place(new Cell(8, 8));
        for (int row = 6; row < game.boardSize(); row++) {
            game.place(new Cell(row, game.boardSize() - 1));
        }
        assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
        assertTrue(game.hasWon(BoardPoint.BLACK));
        assertTrue(game.findWinnerPath(BoardPoint.BLACK, new Cell(0, 0)));
        assertTrue(game.findWinnerPath(BoardPoint.BLACK, new Cell(0, 2)));
    }

    @Test
    void noWinnerTest() {
        assertFalse(game.hasWon(BoardPoint.BLACK));
    }
}
