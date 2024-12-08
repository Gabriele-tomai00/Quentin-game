package quentin;

public class Game {

  private final Player white;
  private final Player black;
  private final Board board;
  private Player currenPlayer;

  public Game() {
    white = new Player(BoardPoint.WHITE);
    black = new Player(BoardPoint.BLACK);
    currenPlayer = black;
    board = new Board();
  }

  public boolean canPlay() {
    for (int row = 0; row < board.SIZE; row++) {
      for (int col = 0; col < board.SIZE; col++) {
        if (board.isMoveValid(currenPlayer, row, col, true)) return true;
      }
    }
    return false;
  }

  public void place(int row, int col) {
    if (board.isMoveValid(currenPlayer, row, col, true))
      board.placeStone(currenPlayer.color(), row, col);
    else throw new IllegalArgumentException("Not a valid move");
  }

  public Player getCurrenPlayer() {
    return currenPlayer;
  }

  public void changeCurrentPlayer() {
    if (currenPlayer.color() == BoardPoint.WHITE) currenPlayer = black;
    else currenPlayer = white;
  }

  public Board getBoard() {
    return board;
  }
}
