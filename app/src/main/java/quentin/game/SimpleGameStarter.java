package quentin.game;

import java.util.Scanner;

public class SimpleGameStarter implements GameStarter {

    protected LocalGame game;

    public SimpleGameStarter() {
        game = new LocalGame();
    }

    @Override
    public void start() {
        display();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (!game.canPlayerPlay()) {
                    game.changeCurrentPlayer();
                    continue;
                }
                if (processInput(scanner)) {
                    break;
                }
                display();
            }
        }
    }

    public boolean processInput(Scanner scanner) {
        while (true) {
            displayMessage(String.format("%s > ", game.getCurrentPlayer()));
            try {
                makeMove(scanner.next());
                break;
            } catch (RuntimeException e) {
                displayMessage(e.getMessage());
            }
        }
        return hasWon();
    }

    public boolean hasWon() {
        if (game.hasWon(game.getCurrentPlayer())) {
            displayWinner();
            return true;
        }
        game.changeCurrentPlayer();
        if (game.hasWon(game.getCurrentPlayer())) {
            displayWinner();
            return true;
        }
        return false;
    }

    public void makeMove(String input) {
        Cell cell = new MoveParser(input).parse();
        game.place(cell);
        game.coverTerritories(cell);
    }

    @Override
    public Game getGame() {
        return game;
    }
}
