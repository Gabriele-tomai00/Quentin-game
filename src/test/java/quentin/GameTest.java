package quentin;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class GameTest {
  Game game = new Game();

  @Test
  public void testMoveIsValid() throws MoveException {

    assertTrue(game.isValid(game.getCurrentPlayer().color(), new Cell(0, 0)));

    game.place(new Cell(0, 0));
    assertFalse(game.isValid(game.getCurrentPlayer().color(), new Cell(1, 1)));
    assertTrue(game.isValid(game.getCurrentPlayer().color(), new Cell(12, 12)));
    game.place(new Cell(0, 1));
    assertTrue(game.isValid(game.getCurrentPlayer().color(), new Cell(1, 1)));
    game.changeCurrentPlayer();
    game.place(new Cell(1, 1));
    game.changeCurrentPlayer();
    assertTrue(game.isValid(game.getCurrentPlayer().color(), new Cell(2, 1)));
    game.place(new Cell(2, 1));
  }

  @Test
  public void canPlayerPlay() {
    assertEquals(true, game.canPlayerPlay());
  }

  @Test
  public void testChangePlayer() {
    assertEquals(BoardPoint.BLACK, game.getCurrentPlayer().color());
    game.changeCurrentPlayer();
    assertEquals(BoardPoint.WHITE, game.getCurrentPlayer().color());
  }

  @Test
  public void playerPlays() throws MoveException {
    Board board = new Board();
    board.placeStone(BoardPoint.BLACK, 0, 0);

    game.place(new Cell(0, 0));

    assertEquals(board, game.getBoard());
    assertThrows(MoveException.class, () -> game.place(new Cell(0, 0)));
  }

  @Test
  public void neighborsTest() {
    Set<Cell> neigbors = new HashSet<Cell>();
    neigbors.add(new Cell(0, 0));
    neigbors.add(new Cell(1, 1));
    neigbors.add(new Cell(2, 0));
    assertEquals(neigbors, game.getNeighbors(new Cell(1, 0)));
  }

  @Test
  public void findNoTerritory() throws MoveException {
    Cell cell = new Cell(0, 0);

    game.place(cell);

    assertEquals(Collections.<Cell>emptySet(), game.findTerritories(cell));
    assertEquals(Collections.emptySet(), game.findTerritories(new Cell(0, 1)));
  }

  @Test
  public void findOneCellTerritory() throws MoveException {

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
  public void findLargeTerritory() throws MoveException {
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
    assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(0, 10)));
    assertEquals(BoardPoint.WHITE, game.getBoard().getPoint(new Cell(0, 8)));
    assertEquals(BoardPoint.EMPTY, game.getBoard().getPoint(new Cell(0, 9)));
    assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(10, 10)));
    assertEquals(testSet, game.findTerritories(new Cell(9, 9)));
  }

  @Test
  public void territoriesAreCovered() {
    try {
      game.place(new Cell(0, 11));
      game.place(new Cell(1, 11));
      game.place(new Cell(1, 12));
      game.coverTerritories(new Cell(1, 12));

      // small 2 cells black territory near top left corner
      game.place(new Cell(0, 0));
      for (int i = 0; i < 4; i++) {
        game.place(new Cell(i, 1));
      }
      game.place(new Cell(3, 0));
      game.coverTerritories(new Cell(3, 0));

      // small 1 cell black territory
      game.place(new Cell(1, 2));
      game.place(new Cell(2, 2));
      game.place(new Cell(3, 2));
      game.place(new Cell(3, 3));
      game.changeCurrentPlayer();
      game.place(new Cell(1, 3));
      game.place(new Cell(1, 4));
      game.place(new Cell(2, 4));
      game.coverTerritories(new Cell(2, 4));
      for (int i = 0; i < game.boardSize(); i++) {
        game.changeCurrentPlayer();
        game.place(new Cell(i, 8));
        game.changeCurrentPlayer();
        game.place(new Cell(i, 10));
      }
      game.coverTerritories(new Cell(12, 10));
    } catch (MoveException e) {
      // do nothing
    }
    assertAll(
        "Territories are being covered",
        () -> assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(0, 12))),
        () -> assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(1, 0))),
        () -> assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(2, 0))),
        () -> assertEquals(BoardPoint.EMPTY, game.getBoard().getPoint(new Cell(0, 3))),
        () -> assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(2, 3))),
        () -> assertEquals(BoardPoint.BLACK, game.getBoard().getPoint(new Cell(0, 9))));
  }

  @Test
  public void letterToIndexTest() {
    assertEquals(0, game.letterToIndex('A'));
    assertEquals(1, game.letterToIndex('B'));
    assertEquals(2, game.letterToIndex('C'));
    assertEquals(3, game.letterToIndex('D'));
    assertEquals(4, game.letterToIndex('E'));
    assertEquals(5, game.letterToIndex('F'));
    assertEquals(6, game.letterToIndex('G'));
    assertEquals(7, game.letterToIndex('H'));
    assertEquals(8, game.letterToIndex('I'));
    assertEquals(9, game.letterToIndex('J'));
    assertEquals(10, game.letterToIndex('K'));
    assertEquals(11, game.letterToIndex('L'));
    assertEquals(12, game.letterToIndex('M'));

    assertEquals(-1, game.letterToIndex('O'));
    assertEquals(-1, game.letterToIndex('Z'));
  }
}
