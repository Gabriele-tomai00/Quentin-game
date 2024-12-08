package quentin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GameTest {
  Game game = new Game();

  @Test
  public void canPlayerPlay() {
    assertEquals(true, game.canPlay());
  }

  @Test
  public void testChangePlayer() {
    assertEquals(BoardPoint.BLACK, game.getCurrenPlayer().color());
    game.changeCurrentPlayer();
    assertEquals(BoardPoint.WHITE, game.getCurrenPlayer().color());
  }

  @Test
  public void playerPlays() {
    Board board = new Board();
    board.placeStone(BoardPoint.BLACK, 0, 0);
    game.place(0, 0);
    assertEquals(board.getBoard()[0][0], game.getBoard().getBoard()[0][0]);
  }
}
