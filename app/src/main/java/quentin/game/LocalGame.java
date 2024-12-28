package quentin.game;

public class LocalGame implements Game {

    private final Player white;
    private final Player black;
    private Player currentPlayer;
    private final Board board;

    public LocalGame() {
        white = new Player(BoardPoint.WHITE);
        black = new Player(BoardPoint.BLACK);
        currentPlayer = black;
        board = new Board();
    }

    public void changeCurrentPlayer() {
        if (getCurrentPlayer().color() == black.color()) {
            currentPlayer = white;
        } else {
            currentPlayer = black;
        }
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
