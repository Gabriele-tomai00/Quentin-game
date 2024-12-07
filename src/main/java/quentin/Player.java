package quentin;

public record Player(BoardPoint color) {

  public Player(BoardPoint color) {
    if (color == BoardPoint.EMPTY)
      throw new IllegalArgumentException(
          "Error during player initialization, empty color not allowed");
    else this.color = color;
  }
}
