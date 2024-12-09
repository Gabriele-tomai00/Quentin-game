package quentin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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

  @Test
  public void neighborsTest() {
    Set<Position> neigbors = new HashSet<Position>();
    neigbors.add(new Position(0, 0));
    neigbors.add(new Position(1, 1));
    neigbors.add(new Position(2, 0));
    assertEquals(neigbors, game.neighbors(new Position(1, 0)));
  }

  @Test
  public void findNoTerritory() {
    game.place(0, 0);
    assertEquals(Collections.<Position>emptySet(), game.findTerritories(0, 0));
    assertEquals(Collections.emptySet(), game.findTerritories(1, 0));
  }

  @Test
  public void findOneCellTerritory() {

    game.place(0, 0);
    game.place(0, 1);
    game.place(1, 1);
    game.place(2, 1);
    game.place(3, 1);
    game.place(3, 0);
    Set<Position> testSet = new HashSet<Position>();
    testSet.add(new Position(1, 0));
    testSet.add(new Position(2, 0));
    assertEquals(testSet, game.findTerritories(1, 0));
  }

  @Test
  public void territoriesAreCovered() {}
}
