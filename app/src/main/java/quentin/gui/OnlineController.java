package quentin.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import quentin.exceptions.MoveException;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.network.CommunicationProtocol;
import quentin.network.NetworkHandler;

public class OnlineController implements Initializable {

    private final List<List<Pane>> panes;
    private OnlineGuiGame game;
    private final NetworkHandler handler;
    @FXML private GridPane board;
    @FXML private GridPane base;
    @FXML private Label textField;
    @FXML private Label messageField;
    @FXML private Button pieButton;
    @FXML private Button exitButton;
    private boolean canPlayPie;
    EventHandler<MouseEvent> eventHandler = this::startWithMouseClick;

    public OnlineController(NetworkHandler handler, OnlineGuiGame game) {
        super();
        this.panes = new ArrayList<>(13);
        this.game = game;
        this.handler = handler;
        canPlayPie = game.getCurrentPlayer().color() == BoardPoint.WHITE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int row = 0; row < 13; row++) {
            panes.add(row, new ArrayList<>(12));
            for (int col = 0; col < 13; col++) {
                panes.get(row).add(new Pane());
                Pane pane = panes.get(row).get(col);
                pane.addEventHandler(MouseEvent.MOUSE_CLICKED, this::placeCell);
                pane.setStyle("-fx-border-color: grey");
                pane.backgroundProperty().bind(game.getBoard().getProperties().get(row).get(col));
                board.add(pane, col, row);
            }
        }
        textField.textProperty().bind(game.getPlayerColor());
        messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, eventHandler);
        game.getSomeoneWon().addListener(_ -> displayWinner());
    }

    public void displayWinner() {
        Platform.runLater(() -> messageField.setText(game.getWinnerMessage().toUpperCase()));
        messageField.toFront();
        base.setEffect(new BoxBlur());
        base.setOpacity(.4);
        messageField.removeEventHandler(MouseEvent.MOUSE_PRESSED, eventHandler);
    }

    public void placeCell(MouseEvent e) {
        if (handler.isWaiting()) {
            errorMessage("Still waiting on opponent's response");
        } else {
            Pane source = (Pane) e.getSource();
            Integer columnIndex = GridPane.getColumnIndex(source);
            Integer rowIndex = GridPane.getRowIndex(source);
            Cell cell = new Cell(rowIndex.intValue(), columnIndex.intValue());
            try {
                canPlayPie = false;
                game.place(cell);
                game.coverTerritories(cell);
                CommunicationProtocol command = new CommunicationProtocol(cell);
                handler.sendCommands(command);
                BoardPoint winner = null;
                if ((winner = someoneWon()) != null) {
                    handler.winner(winner == game.getCurrentPlayer().color() ? "lost" : "won");
                }
            } catch (MoveException e1) {
                errorMessage("Invalid move!");
            }
        }
    }

    public BoardPoint someoneWon() {
        if (game.hasWon(BoardPoint.BLACK)) {
            return BoardPoint.BLACK;
        }
        if (game.hasWon(BoardPoint.WHITE)) {
            return BoardPoint.WHITE;
        }
        return null;
    }

    public void playPie() {
        if (canPlayPie && !handler.isWaiting()) {
            canPlayPie = false;
            game.applyPieRule();
            handler.sendCommands(CommunicationProtocol.pie());
        } else {
            errorMessage("Cannot apply pie rule!!!!");
        }
    }

    public void start() {
        messageField.toBack();
        messageField.setText(null);
        base.setEffect(null);
        base.setOpacity(1);
        base.toFront();
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
