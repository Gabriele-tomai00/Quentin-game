/* (C)2024 */
package quentin;

public class LocalGame extends Game {

    private final Player white;
    private final Player black;

    public LocalGame() {
        super();
        white = new Player(BoardPoint.WHITE);
        black = new Player(BoardPoint.BLACK);
    }

    public void changeCurrentPlayer() {
        if (getCurrentPlayer().color() == black.color()) {
            currentPlayer = white;
        } else {
            currentPlayer = black;
        }
    }
}
