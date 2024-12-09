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
  public void findLargeTerritory() {
    for (int i = 0; i < game.getBoard().SIZE; i++) {
      game.place(i, 10);
      game.changeCurrentPlayer();
      game.place(i, 8);
      game.changeCurrentPlayer();
    }
    HashSet<Position> testSet = new HashSet<Position>();
    for (int i = 0; i < game.getBoard().SIZE; i++) {
      testSet.add(new Position(i, 9));
    }
    assertEquals(BoardPoint.BLACK, game.getBoard().getValues(0, 10));
    assertEquals(BoardPoint.WHITE, game.getBoard().getValues(0, 8));
    assertEquals(BoardPoint.EMPTY, game.getBoard().getValues(0, 9));
    assertEquals(BoardPoint.BLACK, game.getBoard().getValues(10, 10));
    assertEquals(testSet, game.findTerritories(3, 9));
  }

  @Test
  public void territoriesAreCovered() {
    game.place(0, 0);
    game.place(0, 1);
    game.place(1, 1);
    game.place(2, 1);
    game.place(3, 1);
    game.place(3, 0);
    game.coverTerritories(0, 0);
    assertEquals(BoardPoint.EMPTY, game.getBoard().getValues(1, 2));
    assertEquals(BoardPoint.EMPTY, game.getBoard().getValues(2, 2));
    assertEquals(BoardPoint.BLACK, game.getBoard().getValues(1, 0));
    assertEquals(BoardPoint.BLACK, game.getBoard().getValues(2, 0));

    game.place(0, 3);
    game.place(2, 3);
    game.place(2, 5);
    game.place(0, 5);
    game.changeCurrentPlayer();
    game.place(1, 3);
    game.place(2, 4);
    game.place(1, 5);
    game.coverTerritories(1, 3);
    assertEquals(BoardPoint.EMPTY, game.getBoard().getValues(0, 2));
    assertEquals(BoardPoint.WHITE, game.getBoard().getValues(0, 4));
    for (int i = 0; i < game.getBoard().SIZE; i++) {
      game.place(i, 10);
      game.changeCurrentPlayer();
      game.place(i, 8);
      game.changeCurrentPlayer();
    }
    game.coverTerritories(0, 10);
    assertEquals(BoardPoint.BLACK, game.getBoard().getValues(0, 9));
  }
}
