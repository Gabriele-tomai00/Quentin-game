package quentin;

public abstract class GameStarter {
    private final Game game;

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
                        Cell cell = getInput();
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

    protected abstract Cell getInput() throws InvalidCellValuesException;

    protected abstract void display();
}
