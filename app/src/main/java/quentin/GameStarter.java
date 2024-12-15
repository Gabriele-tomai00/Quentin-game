package quentin;

public abstract class GameStarter<T> {
    private final Game game;
    private T input;

    public GameStarter() {
        game = new Game();
    }

    public void start() {

        startDisplay(); // System.out.println(game.getBoard());

        while (true) {
            if (game.canPlayerPlay()) {
                displayMessage(String.format("%s turn:", game.getCurrentPlayer()));
                while (true) {
                    try {
                        Cell cell = getInput(input);
                        game.place(cell);
                        game.coverTerritories(cell);
                        break;
                    } catch (MoveException e) {
                        System.out.println(e);
                    } catch (InvalidCellValuesException e) {
                        System.out.println(e);
                    }
                }
                display(); // System.out.println(game.getBoard());
            }
            if (game.getBoard().hasWon(game.getCurrentPlayer().color())) {
                displayWinner();
                // System.out.printf("%s has won", game.getCurrentPlayer());
                break;
            }
            game.changeCurrentPlayer();
            if (game.getBoard().hasWon(game.getCurrentPlayer().color())) {
                displayWinner();
                // System.out.printf("%s has won", game.getCurrentPlayer());
                break;
            }
        }
    }

    public Game game() {
        return game;
    }

    protected abstract void startDisplay();

    protected abstract void displayMessage(String format);

    protected abstract void displayWinner();

    protected abstract Cell getInput(T input) throws InvalidCellValuesException;

    protected abstract void display();
}
