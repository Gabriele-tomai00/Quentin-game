package quentin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class GameTest {
  Game game = new Game();
  
  @te

  @Test
  public void canPlayerPlay() {
    assertEquals(true, game.canPlay());
  }

  @Test
  public void testChangePlayer() {
    assertEquals(BoardPoint.BLACK, game.getCurrenPlayer()
                                       .color());
    game.changeCurrentPlayer();
    assertEquals(BoardPoint.WHITE, game.getCurrenPlayer()
                                       .color());
  }

  @Test
  public void playerPlays() {
    Board board = new Board();
    board.placeStone(BoardPoint.BLACK, 0, 0);
    game.place(new Cell(0, 0));
    assertEquals(board.getBoard()[0][0], game.getBoard()
                                             .getBoard()[0][0]);
  }

  @Test
  public void neighborsTest() {
    Set<Cell> neigbors = new HashSet<Cell>();
    neigbors.add(new Cell(0, 0));
    neigbors.add(new Cell(1, 1));
    neigbors.add(new Cell(2, 0));
    assertEquals(neigbors, game.neighbors(new Cell(1, 0)));
  }

  @Test
  public void findNoTerritory() {
    Cell cell = new Cell(0, 0);
    game.place(cell);
    assertEquals(Collections.<Cell>emptySet(), game.findTerritories(cell));
    assertEquals(Collections.emptySet(), game.findTerritories(new Cell(0, 1)));
  }

  @Test
  public void findOneCellTerritory() {

    game.place(new Cell(0, 0));
    game.place(new Cell(0, 1));
    game.place(new Cell(1, 1));
    game.place(new Cell(2, 1));
    game.place(new Cell(3, 1));
    game.place(new Cell(3, 0));
    Set<Cell> testSet = new HashSet<Cell>();
    testSet.add(new Cell(1, 0));
    testSet.add(new Cell(2, 0));
    assertEquals(testSet, game.findTerritories(new Cell(1, 0)));
  }

  @Test
  public void findLargeTerritory() {
    for (int i = 0; i < game.boardSize(); i++) {
      game.place(new Cell(i, 10));
      game.changeCurrentPlayer();
      game.place(new Cell(i, 8));
      game.changeCurrentPlayer();
    }
    HashSet<Cell> testSet = new HashSet<Cell>();
    for (int i = 0; i < game.boardSize(); i++) {
      testSet.add(new Cell(i, 9));
    }
    assertEquals(BoardPoint.BLACK, game.getBoard()
                                       .getValues(0, 10));
    assertEquals(BoardPoint.WHITE, game.getBoard()
                                       .getValues(0, 8));
    assertEquals(BoardPoint.EMPTY, game.getBoard()
                                       .getValues(0, 9));
    assertEquals(BoardPoint.BLACK, game.getBoard()
                                       .getValues(10, 10));
    assertEquals(testSet, game.findTerritories(new Cell(9, 9)));
  }

  @Test
  public void territoriesAreCovered() {
    game.place(new Cell(0, 0));
    for (int i = 0; i < 4; i++) {
      game.place(new Cell(i, 1));
    }
    game.place(new Cell(3, 0));
    game.coverTerritories(new Cell(3, 0));
    assertEquals(BoardPoint.EMPTY, game.getBoard()
                                       .getValues(1, 2));
    assertEquals(BoardPoint.EMPTY, game.getBoard()
                                       .getValues(2, 2));
    assertEquals(BoardPoint.BLACK, game.getBoard()
                                       .getValues(1, 0));
    assertEquals(BoardPoint.BLACK, game.getBoard());

    game.place(new Cell(1, 2));
    game.place(new Cell(2, 2));
    game.place(new Cell(3, 2));
    game.place(new Cell(3, 3));
    game.changeCurrentPlayer();
    game.place(new Cell(1, 3));
    game.place(new Cell(1, 4));
    game.place(new Cell(2, 4));
    game.place(new Cell(3, 4));
    game.coverTerritories(new Cell(3, 4));
    assertEquals(BoardPoint.EMPTY, game.getBoard()
                                       .getValues(0, 2));
    assertEquals(BoardPoint.WHITE, game.getBoard()
                                       .getValues(0, 4));
    for (int i = 0; i < game.boardSize(); i++) {
      game.place(new Cell(i, 8));
      game.changeCurrentPlayer();
      game.place(new Cell(i, 10));
      game.changeCurrentPlayer();
    }
    game.coverTerritories(new Cell(12, 10));
    assertEquals(BoardPoint.BLACK, game.getBoard()
                                       .getValues(0, 9));
  }
}
