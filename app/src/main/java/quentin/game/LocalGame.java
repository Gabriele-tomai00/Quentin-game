package quentin.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LocalGame implements Game {

    private static final long serialVersionUID = -8782140056307981297L;
    private static final Player white = new Player(BoardPoint.WHITE);
    private static final Player black = new Player(BoardPoint.BLACK);
    private Player currentPlayer;
    private transient Board board;

    public LocalGame() {
        currentPlayer = black;
        board = new Board();
    }

    public LocalGame(LocalGame game) {
        this.board = new Board();
        board.setBoard(game.getBoard());
        this.currentPlayer = new Player(game.getCurrentPlayer().color());
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Game game) {
            if (this.getBoard().equals(game.getBoard())
                    && this.getCurrentPlayer().equals(game.getCurrentPlayer())) {
                return true;
            }
        }
        return false;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(board.toCompactString());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        String compactString = (String) ois.readObject();
        this.board = new Board();
        this.board.fromCompactString(compactString);
    }
}
