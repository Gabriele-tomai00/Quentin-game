package quentin.gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
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
import quentin.game.LocalGame;

public class Controller implements Initializable {

    private final List<List<Pane>> panes;
    private LocalGame game;
    @FXML private GridPane board;
    @FXML private GridPane base;
    @FXML private Label textField;
    @FXML private Button resetButton;
    @FXML private Label messageField;
    @FXML private Button goBackButton;
    @FXML private Button goForwardButton;
    @FXML private Button exitButton;
    private final Cache<GameLog> cache;
    EventHandler<MouseEvent> startHandler = this::startWithMouseClick;
    EventHandler<MouseEvent> resetHandler = this::resetWithMouseClicked;

    public Controller() {
        super();
        this.panes = new ArrayList<>(13);
        this.game = new LocalGame();
        cache = new Cache<>();
    }

    public Controller(Cache<GameLog> cache) {
        super();
        panes = new ArrayList<>(13);
        if (cache.getMemorySize() > 0) {
            game = new LocalGame(cache.getLog().game());
        } else {
            game = new LocalGame();
        }
        this.cache = cache;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int row = 0; row < 13; row++) {
            panes.add(row, new ArrayList<>(13));
            for (int col = 0; col < 13; col++) {
                panes.get(row).add(col, new Pane());
                Pane pane = panes.get(row).get(col);
                pane.addEventHandler(MouseEvent.MOUSE_CLICKED, this::placeCell);
                pane.setStyle("-fx-border-color: grey");
                board.add(pane, col, row);
            }
        }
        messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, startHandler);
    }

    public void displayWinner() {
        messageField.setText((game.getCurrentPlayer() + " wins").toUpperCase());
        messageField.removeEventHandler(MouseEvent.MOUSE_PRESSED, startHandler);
        messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, resetHandler);
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
            if (game.hasWon(game.getCurrentPlayer().color())) {
                displayWinner();
            }
            game.changeCurrentPlayer();
            if (game.hasWon(game.getCurrentPlayer().color())) {
                displayWinner();
            }
            cache.saveLog(new GameLog(LocalDateTime.now(), new LocalGame(game)));
            display();
        } catch (MoveException e1) {
            errorMessage("Invalid move!");
        }
    }

    public void display() {
        for (int row = 0; row < 13; row++) {
            for (int col = 0; col < 13; col++) {
                BoardPoint colorPoint = game.getBoard().getPoint(new Cell(row, col));
                ObjectProperty<Background> backgroundProperty =
                        panes.get(row).get(col).backgroundProperty();
                switch (colorPoint) {
                    case BoardPoint.BLACK -> backgroundProperty.set(Background.fill(Color.BLACK));
                    case BoardPoint.WHITE -> backgroundProperty.set(Background.fill(Color.WHITE));
                    default -> backgroundProperty.set(Background.EMPTY);
                }
            }
        }
        textField.setText(game.getCurrentPlayer() + "'s turn!!!");
    }

    public void reset() {
        game = new LocalGame();
        cache.clear();
        messageField.toFront();
        messageField.setText("Click anywhere to start");
        messageField.removeEventHandler(MouseEvent.MOUSE_PRESSED, resetHandler);
        messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, startHandler);
        base.setOpacity(.4);
        base.setEffect(new BoxBlur());
        display();
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
            display();
        } catch (RuntimeException ex) {
            errorMessage("No more memory left!");
        }
    }

    public void goForward() {
        try {
            game = new LocalGame(cache.goForward().game());
            display();
        } catch (RuntimeException ex) {
            errorMessage("Cannot go forward!");
        }
    }

    public Cache<GameLog> getCache() {
        return cache;
    }

    public void start() {
        messageField.toBack();
        messageField.setText(null);
        base.setEffect(null);
        base.setOpacity(1);
        base.toFront();
        display();
    }

    public void startWithMouseClick(MouseEvent e) {
        start();
    }

    public void errorMessage(String exception) {
        messageField.setText(exception);
        messageField.setTextFill(Color.RED);
        messageField.toFront();
        messageField.setFont(Font.font("Menlo bold", 20));
        messageField.setBackground(Background.fill(Color.WHITE));
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

    public void exitGame() {
        Platform.exit();
    }
}
