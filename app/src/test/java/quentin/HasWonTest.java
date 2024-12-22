package quentin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class HasWonTest {

  Game game = new Game();

  @Test
  public void hasWonException() {
    assertFalse(game.hasWon(game.getCurrentPlayer()));
  }

  @Test
  public void findWhiteLine() throws MoveException {
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W" },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." }
    game.changeCurrentPlayer();
    assertEquals(BoardPoint.WHITE, game.getCurrentPlayer()
                                       .color());

    for (Cell cell = new Cell(0, 0); cell.col() < game.boardSize(); cell.setCol(cell.col() + 1)) { game.place(cell); }
    assertTrue(game.hasWon(game.getCurrentPlayer()));
  }

  @Test
  public void findWhite2Lines() throws MoveException {
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W" },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W" },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." }
    game.changeCurrentPlayer();
    Cell first = new Cell(3, 0);
    Cell second = new Cell(6, 0);
    for (int i = 0; i < game.boardSize(); i++, first.setCol(i), second.setCol(i)) {
      game.place(second);
      game.place(first);
    }
    assertEquals(BoardPoint.WHITE, game.getCurrentPlayer()
                                       .color());
    assertTrue(game.findWinnerPath(BoardPoint.WHITE, new Cell(4, 0)));
    assertTrue(game.findWinnerPath(BoardPoint.WHITE, new Cell(6, 0)));
  }

  @Test
  public void findWhiteOrthogonalDownLines() throws MoveException {
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "W", "W", "W", "W", "W", "W", "W", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", "W", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", "W", "W", "W", "W", "W", "W", "W" },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." }
    game.changeCurrentPlayer();
    assertEquals(BoardPoint.WHITE, game.getCurrentPlayer()
                                       .color());
    for (Cell cell = new Cell(4, 0); cell.col() < game.boardSize(); cell.setCol(cell.col() + 1)) {
      game.place(cell);
      if (cell.col() == 6) {
        game.place(new Cell(5, 6));
        game.place(new Cell(6, 6));
        cell.setRow(6);
      }

    }
    assertTrue(game.hasWon(game.getCurrentPlayer()));
  }

  /*
   * @Test public void findWhiteOrthogonalUpLines() { String[][] matrix1 = { {
   * ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." }, { ".",
   * ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." }, { ".", ".",
   * ".", ".", ".", ".", "W", "W", "W", "W", "W", "W", "W" }, { ".", ".", ".",
   * ".", ".", ".", "W", ".", ".", ".", ".", ".", "." }, { "W", "W", "W", "W",
   * "W", "W", "W", ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".",
   * ".", ".", ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", ".",
   * ".", ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", ".", ".",
   * ".", ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", ".", ".", ".",
   * ".", ".", ".", ".", "." }, { ".", ".", ".", ".", ".", ".", ".", ".", ".",
   * ".", ".", ".", "." }, { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".",
   * ".", ".", "." }, { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".",
   * ".", "." }, { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "."
   * } }; Board board1 = new Board(matrix1);
   * assertTrue(game.hasWon(BoardPoint.WHITE)); }
   */

  @Test
  public void findWhiteLinesWithLoops() throws MoveException {
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", "W", "W", "W", "W", "W", "W", "W" },
    // { ".", ".", ".", ".", ".", ".", "W", ".", "W", ".", ".", ".", "." },
    // { "W", "W", "W", "W", "W", "W", "W", "W", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." }
    game.changeCurrentPlayer();
    assertEquals(BoardPoint.WHITE, game.getCurrentPlayer()
                                       .color());
    for (int i = 0; i < 9; i++) { game.place(new Cell(4, i)); }
    for (int i = 6; i < game.boardSize(); i++) { game.place(new Cell(2, i)); }
    game.place(new Cell(3, 6));
    game.place(new Cell(3, 8));
    assertTrue(game.hasWon(game.getCurrentPlayer()));
  }

  @Test
  public void findWhiteDifficultPath() throws MoveException {
    // { "W", "W", "W", "W", "W", "W", "W", "W", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", "W", "W", "W", "W", "W", "W", "." },
    // { ".", ".", ".", ".", ".", ".", "W", ".", "W", ".", ".", ".", "." },
    // { "W", "W", "W", "W", "W", "W", "W", "W", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", "W", ".", ".", ".", "." },
    // { ".", "W", "W", "W", "W", "W", "W", "W", "W", ".", ".", ".", "." },
    // { ".", "W", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", "W", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", "W", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", "W", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W" }
    game.changeCurrentPlayer();
    assertEquals(BoardPoint.WHITE, game.getCurrentPlayer()
                                       .color());
    for (int j = 1; j < 9; j++) {
      game.place(new Cell(0, j));
      game.place(new Cell(4, j));
      game.place(new Cell(7, j));
    }
    game.place(new Cell(0, 0));
    game.place(new Cell(4, 0));
    for (int j = 1; j < game.boardSize(); j++) { game.place(new Cell(game.boardSize() - 1, j)); }
    for (int i = 0; i < 8; i++) {
      if (i == 0 || i == 4 || i == 7) { continue; }
      game.place(new Cell(i, 8));
    }
    for (int i = 8; i < game.boardSize() - 1; i++) { game.place(new Cell(i, 1)); }
    for (int j = 6; j < game.boardSize() - 1; j++) {
      if (j == 8) { continue; }
      game.place(new Cell(2, j));
    }
    game.place(new Cell(3, 6));
    assertTrue(game.findWinnerPath(game.getCurrentPlayer()
                                       .color(),
        new Cell(0, 0)));
    assertTrue(game.findWinnerPath(game.getCurrentPlayer()
                                       .color(),
        new Cell(4, 0)));
    assertTrue(game.hasWon(game.getCurrentPlayer()));
  }

  @Test
  public void findWhitePathInNormalSituation() throws MoveException {
    // { "W", "W", "W", "W", "W", "W", "W", "W", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", "W", "W", "W", "W", "W", "W", "B" },
    // { ".", ".", ".", ".", "B", ".", "W", ".", "W", ".", ".", ".", "." },
    // { "W", "W", "W", "W", "W", "W", "W", "W", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", "W", ".", ".", ".", "." },
    // { ".", ".", ".", ".", ".", ".", ".", ".", "W", ".", ".", ".", "." },
    // { ".", "W", "W", "W", "W", "W", "W", "W", "W", ".", "B", ".", "." },
    // { ".", "W", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", "W", ".", "B", "B", "B", ".", ".", ".", ".", ".", ".", "." },
    // { ".", "W", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", "." },
    // { ".", "W", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W" }
    for (int i = 1; i < 4; i++) { game.place(new Cell(i, 4)); }
    for (Cell cell = new Cell(8, 3); cell.row() < 11; cell.setRow(cell.row() + 1)) {
      game.place(cell);
      if (cell.row() == 9) {
        game.place(new Cell(4, 9));
        game.place(new Cell(5, 9));
        cell.setCol(5);
      }
    }
    game.place(new Cell(2, game.boardSize() - 1));
    game.place(new Cell(7, 10));
    game.changeCurrentPlayer();
    for (int j = 1; j < 9; j++) {
      game.place(new Cell(0, j));
      game.place(new Cell(4, j));
      game.place(new Cell(7, j));
    }
    game.place(new Cell(0, 0));
    game.place(new Cell(4, 0));
    for (int j = 1; j < game.boardSize(); j++) { game.place(new Cell(game.boardSize() - 1, j)); }
    for (int i = 0; i < 8; i++) {
      if (i == 0 || i == 4 || i == 7) { continue; }
      game.place(new Cell(i, 8));
    }
    for (int i = 8; i < game.boardSize() - 1; i++) { game.place(new Cell(i, 1)); }
    for (int j = 6; j < game.boardSize() - 1; j++) {
      if (j == 8) { continue; }
      game.place(new Cell(2, j));
    }
    game.place(new Cell(3, 6));
    assertEquals(BoardPoint.WHITE, game.getCurrentPlayer()
                                       .color());
    assertTrue(game.hasWon(game.getCurrentPlayer()));
  }

  @Test
  public void findBlackLine() throws MoveException {
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." }
    for (Cell cell = new Cell(0, 4); cell.row() < game.boardSize(); cell.setRow(cell.row() + 1)) { game.place(cell); }
    assertEquals(BoardPoint.BLACK, game.getCurrentPlayer()
                                       .color());
    assertTrue(game.hasWon(game.getCurrentPlayer()));
  }

  @Test
  public void findBlack2Lines() throws MoveException {
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." }
    Cell first = new Cell(0, 3);
    Cell second = new Cell(0, 6);
    for (int i = 0; i < game.boardSize(); i++, first.setRow(i), second.setRow(i)) {
      game.place(second);
      game.place(first);
    }
    assertEquals(BoardPoint.BLACK, game.getCurrentPlayer()
                                       .color());
    assertTrue(game.findWinnerPath(BoardPoint.BLACK, new Cell(0, 2)));
    assertTrue(game.findWinnerPath(BoardPoint.BLACK, new Cell(0, 4)));
  }

  @Test
  public void findDarkOrthogonalLeftLines() throws MoveException {
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", "B", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." }
    for (Cell cell = new Cell(0, 4); cell.row() < game.boardSize(); cell.setRow(cell.row() + 1)) {
      game.place(cell);
      if (cell.row() == 5) {
        game.place(new Cell(5, 3));
        game.place(new Cell(5, 2));
        cell.setCol(2);
      }
    }
    assertEquals(BoardPoint.BLACK, game.getCurrentPlayer()
                                       .color());
    assertTrue(game.hasWon(game.getCurrentPlayer()));
  }

  @Test
  public void findDarkOrthogonalRightLines() throws MoveException {
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", "B", "B", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { ".", ".", ".", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." }
    for (Cell cell = new Cell(0, 2); cell.row() < game.boardSize(); cell.setRow(cell.row() + 1)) {
      game.place(cell);
      if (cell.row() == 5) {
        game.place(new Cell(5, 3));
        game.place(new Cell(5, 4));
        cell.setCol(4);
      }
    }
    assertEquals(BoardPoint.BLACK, game.getCurrentPlayer()
                                       .color());
    assertTrue(game.hasWon(game.getCurrentPlayer()));
  }

  @Test
  public void findDarkOrthogonalDifficultLines() throws MoveException {
    // { "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "B", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "B", ".", "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", "." },
    // { "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B" },
    // { ".", ".", ".", ".", "B", ".", ".", "B", ".", "B", ".", ".", "B" },
    // { "B", ".", "B", ".", "B", ".", ".", "B", ".", "B", ".", ".", "B" },
    // { "B", ".", "B", ".", ".", ".", ".", "B", "B", "B", ".", ".", "B" },
    // { "B", ".", "B", ".", ".", ".", ".", ".", ".", ".", ".", ".", "B" },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "B" },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "B" },
    // { ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", ".", "B" }
    for (int i = 0; i < 10; i++) {
      if (i == 6) { continue; }
      game.place(new Cell(i, 0));
      game.place(new Cell(i, 2));
    }
    for (Cell cell = new Cell(5, 1); cell.col() < game.boardSize(); cell.setCol(cell.col() + 1)) {
      if (cell.col() == 2) { continue; }
      game.place(cell);
    }
    for (int i = 3; i < 8; i++) {
      if (i == 5) { continue; }
      game.place(new Cell(i, 4));
    }
    for (int i = 6; i < 10; i++) {
      game.place(new Cell(i, 7));
      game.place(new Cell(i, 9));
    }
    game.place(new Cell(8, 8));
    for (int i = 6; i < game.boardSize(); i++) { game.place(new Cell(i, game.boardSize() - 1)); }
    assertEquals(BoardPoint.BLACK, game.getCurrentPlayer()
                                       .color());
    assertTrue(game.hasWon(game.getCurrentPlayer()));
    assertTrue(game.findWinnerPath(BoardPoint.BLACK, new Cell(0, 0)));
    assertTrue(game.findWinnerPath(BoardPoint.BLACK, new Cell(0, 2)));
  }
}
