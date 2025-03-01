package quentin.gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import quentin.cache.Cache;
import quentin.cache.GameLog;
import quentin.exceptions.MoveException;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.Game;
import quentin.game.LocalGame;

public class Controller implements Initializable {

    private final Pane[][] panes;
    private LocalGame game;
    @FXML private GridPane board;
    @FXML private GridPane base;
    @FXML private Label textField;
    @FXML private Button resetButton;
    @FXML private Label messageField;
    @FXML private Button goBackButton;
    @FXML private Button goForwardButton;
    @FXML private Button exitButton;
    private final Background black = Background.fill(Color.BLACK);
    private final Background white = Background.fill(Color.WHITE);
    private final Cache<GameLog> cache;

    public Controller() {
        super();
        this.panes = new Pane[13][13];
        this.game = new LocalGame();
        cache = new Cache<>();
    }

    public Controller(Cache<GameLog> cache) {
        super();
        panes = new Pane[13][13];
        if (cache.getMemorySize() > 0) {
            game = cache.getLog().game();
        } else {
            game = new LocalGame();
        }
        this.cache = cache;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                panes[i][j] = new Pane();
                panes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, this::placeCell);
                panes[i][j].setStyle("-fx-border-color: grey");
                board.add(panes[i][j], j, i);
            }
        }
        messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startWithMouseClick);
    }

    public void displayMessage(String message) {
        textField.setText(message);
    }

    public void display() {
        BoardPoint[][] gameBoard = game.getBoard().getBoard();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                panes[i][j].setStyle("-fx-border-color: grey");
                if (gameBoard[i][j] == BoardPoint.BLACK) {
                    panes[i][j].setBackground(black);
                } else if (gameBoard[i][j] == BoardPoint.WHITE) {
                    panes[i][j].setBackground(white);
                } else {
                    panes[i][j].setBackground(Background.EMPTY);
                }
            }
        }
    }

    public void start() {
        displayMessage(displayPlayerName());
        messageField.toBack();
        messageField.setText(null);
        base.setEffect(null);
        base.setOpacity(1);
        base.toFront();
        display();
    }

    private String displayPlayerName() {
        return game.getCurrentPlayer() + "'s turn!";
    }

    public void startWithMouseClick(MouseEvent e) {
        start();
    }

    public void displayWinner() {
        messageField.setText((game.getCurrentPlayer() + " wins").toUpperCase());
        messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, this::resetWithMouseClicked);
        messageField.toFront();
        base.setEffect(new BoxBlur());
        base.setOpacity(.4);
        cache.clear();
    }

    public void placeCell(MouseEvent e) {
        Pane source = (Pane) e.getSource();
        Integer columnIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        Cell cell = new Cell(rowIndex.intValue(), columnIndex.intValue());
        try {
            game.place(cell);
            game.coverTerritories(cell);

            display();
            source.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
            if (game.hasWon(game.getCurrentPlayer())) {
                displayWinner();
            }
            game.changeCurrentPlayer();
            if (game.hasWon(game.getCurrentPlayer())) {
                displayWinner();
            }
            cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
            displayMessage(displayPlayerName());
        } catch (MoveException e1) {
            errorMessage("Invalid move!");
        }
    }

    public void errorMessage(String exception) {
        messageField.setText(exception);
        messageField.setTextFill(Color.RED);
        messageField.toFront();
        messageField.setFont(Font.font("Menlo bold", 20));
        messageField.setBackground(white);
        messageField.setOpacity(.8);
        PauseTransition transition = new PauseTransition(Duration.millis(1000));
        transition.setOnFinished(
                _ -> {
                    messageField.setTextFill(Color.WHITE);
                    messageField.toBack();
                    messageField.setFont(Font.font("Menlo bold", 36));
                    base.setOpacity(1);
                    messageField.setBackground(null);
                    messageField.setOpacity(1);
                });
        transition.play();
    }

    public void reset() {
        game = new LocalGame();
        cache.clear();
        messageField.toFront();
        messageField.setText("Click anywhere to start");
        messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startWithMouseClick);
        base.setOpacity(.4);
        base.setEffect(new BoxBlur());
        textField.setText(null);
    }

    public void resetWithMouseClicked(MouseEvent e) {
        reset();
    }

    public void resetWithButtonPressed() {
        reset();
    }

    public void goBack() {
        try {
            game = new LocalGame(cache.goBack().game());
            displayMessage(displayPlayerName());
        } catch (RuntimeException ex) {
            errorMessage("No more memory left!");
        }
        display();
    }

    public void goForward() {
        try {
            game = new LocalGame(cache.goForward().game());
            displayMessage(displayPlayerName());
        } catch (RuntimeException ex) {
            errorMessage("Cannot go forward!");
        }
        display();
    }

    public void exitGame() {
        Platform.exit();
    }

    public Cache<GameLog> getCache() {
        return cache;
    }

    public Game getGame() {
        return game;
    }
}
