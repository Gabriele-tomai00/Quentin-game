package quentin.game;

public interface GameStarter {

    static final String CLEAR = "\033[H\033[2J";

    default void displayMessage(String format) {
        System.out.print(format);
    }

    default void displayWinner() {
        System.out.println(
                CLEAR + String.format("%s has won", getGame().getCurrentPlayer()).toUpperCase());
    }

    default void displayWinner(Player player) {
        System.out.println(CLEAR + String.format("%s has won", player).toUpperCase());
    }

    default void display() {
        System.out.println(CLEAR + getGame().getBoard());
    }

    void start();

    public Game getGame();
}
