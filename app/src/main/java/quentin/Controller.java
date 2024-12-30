package quentin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import quentin.cache.Cache;
import quentin.exceptions.MoveException;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.GameStarter;
import quentin.game.LocalGame;

public class Controller implements GameStarter {

    private final Pane[][] panes;
    private LocalGame game;
    @FXML private GridPane board;
    @FXML private GridPane base;
    @FXML private Label textField;
    @FXML private Label winnerText;

    @FXML private Button displayWinner;
    @FXML private Button reset;
    @FXML private Label startPane;
    @FXML private Button goBack;
    @FXML private Button goForward;
    private Background black = Background.fill(Color.BLACK);
    private Background white = Background.fill(Color.WHITE);
    private Cache<LocalGame> cache;

    public Controller() {
        super();
        this.panes = new Pane[13][13];
        this.game = new LocalGame();
        cache = new Cache<>();
    }

    @FXML
    public void initialize() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                panes[i][j] = new Pane();
                panes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, this::placeCell);
                panes[i][j].setStyle("-fx-border-color: grey");
                // pane.setPrefSize(30, 30);
                board.add(panes[i][j], j, i);
            }
        }
    }

    public void start() {
        displayMessage(game.getCurrentPlayer() + "'s turn!");
        startPane.toBack();
        base.setEffect(null);
        base.setOpacity(1);
        display();
    }

    @Override
    public void displayMessage(String message) {
        textField.setText(message);
    }

    public void displayWinner() {
        winnerText.setText(new String(game.getCurrentPlayer() + " wins").toUpperCase());
        winnerText.toFront();
        base.setEffect(new BoxBlur());
        base.setOpacity(.5);
        startPane.setOpacity(0);
    }

    public void display() {

        BoardPoint[][] board = game.getBoard().getBoard();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if (board[i][j] == BoardPoint.BLACK) {
                    panes[i][j].setBackground(black);
                } else if (board[i][j] == BoardPoint.WHITE) {
                    panes[i][j].setBackground(white);
                } else {
                    panes[i][j].setBackground(Background.EMPTY);
                }
            }
        }
    }

    public void goBack(ActionEvent e) {
        try {
            game = cache.goBack();
            displayMessage(game.getCurrentPlayer() + "'s turn!");
        } catch (RuntimeException ex) {
            displayMessage(ex.getMessage());
        }
        display();
    }

    public void goForward(ActionEvent e) {
        try {
            game = cache.goForward();
            displayMessage(game.getCurrentPlayer() + "'s turn!");
        } catch (RuntimeException ex) {
            displayMessage(ex.getMessage());
        }
        display();
    }

    protected void placeCell(MouseEvent e) {
        Pane source = (Pane) e.getSource();
        Integer columnIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        Cell cell = new Cell(rowIndex.intValue(), columnIndex.intValue());
        try {
            game.place(cell);
            game.coverTerritories(cell);
            display();
            if (game.hasWon(game.getCurrentPlayer())) {
                displayWinner();
            }
            game.changeCurrentPlayer();
            if (game.hasWon(game.getCurrentPlayer())) {
                displayWinner();
            }
            cache.saveLog(new LocalGame(game));
            displayMessage(game.getCurrentPlayer() + "'s turn!");
        } catch (MoveException e1) {
            displayMessage(e1.getMessage());
        }
    }

    public void reset(ActionEvent e) {
        game = new LocalGame();
        winnerText.toBack();
        startPane.toFront();
        base.setOpacity(.5);
        base.setEffect(new BoxBlur());
        winnerText.setText("");
        startPane.setOpacity(1);
        textField.setText(null);
    }

    public void displayWinner(ActionEvent e) {
        displayWinner();
    }

    @Override
    public void startDisplay() {
        // TODO Auto-generated method stub

    }
}
