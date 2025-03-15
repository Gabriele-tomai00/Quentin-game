package quentin.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import quentin.game.Cell;
import quentin.game.Game;
import quentin.network.NetworkHandler;

public class OnlineController implements Initializable {

  private final Pane[][] panes;
  private final OnlineGuiGame game;
  private final NetworkHandler handler;
  @FXML private GridPane board;
  @FXML private GridPane base;
  @FXML private Label textField;
  @FXML private Label messageField;
  @FXML private Button pieButton;
  @FXML private Button exitButton;

  public OnlineController(NetworkHandler handler, OnlineGuiGame game) {
    super();
    this.panes = new Pane[13][13];
    this.game = game;
    this.handler = handler;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    for (int i = 0; i < 13; i++) {
      for (int j = 0; j < 13; j++) {
        panes[i][j] = new Pane();
        panes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, this::placeCell);
        panes[i][j].setStyle("-fx-border-color: grey");
        panes[i][j].backgroundProperty()
                   .bind(game.getBoard()
                             .getProperties()[i][j]);
        board.add(panes[i][j], j, i);
      }
    }
    textField.setText(game.getCurrentPlayer() + " player!!!");
    messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startWithMouseClick);
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

  public void displayWinner() {
    messageField.setText((game.getCurrentPlayer() + " wins").toUpperCase());
    // messageField.addEventHandler(MouseEvent.MOUSE_PRESSED,
    // this::resetWithMouseClicked);
    messageField.toFront();
    base.setEffect(new BoxBlur());
    base.setOpacity(.4);
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
        game.place(cell);
        game.coverTerritories(cell);
        if (game.hasWon(game.getCurrentPlayer())) { displayWinner(); }
        // dio lampione
        int row = cell.col() + 'a';
        String command = String.valueOf((char) row)
                               .concat(cell.row() + 1 + "");
        System.out.println(command);
        handler.sendCommands(command);
        if (game.hasWon(game.getCurrentPlayer())) { displayWinner(); }

      } catch (MoveException e1) {
        errorMessage("Invalid move!");
      }
    }
  }

  public void errorMessage(String exception) {
    messageField.setText(exception);
    messageField.setTextFill(Color.RED);
    messageField.toFront();
    messageField.setFont(Font.font("Menlo bold", 20));
    messageField.setBackground(Background.fill(Color.WHITE));
    messageField.setOpacity(.8);
    PauseTransition transition = new PauseTransition(Duration.millis(1000));
    transition.setOnFinished(_ -> {
      messageField.setTextFill(Color.WHITE);
      messageField.toBack();
      messageField.setFont(Font.font("Menlo bold", 36));
      base.setOpacity(1);
      messageField.setBackground(null);
      messageField.setOpacity(1);
    });
    transition.play();
  }

  // public void reset() {
  // game = new OnlineGuiGame();
  // messageField.toFront();
  // messageField.setText("Click anywhere to start");
  // messageField.addEventHandler(MouseEvent.MOUSE_PRESSED,
  // this::startWithMouseClick);
  // base.setOpacity(.4);
  // base.setEffect(new BoxBlur());
  // }

  // public void resetWithMouseClicked(MouseEvent e) {
  // reset();
  // }
  //
  // public void resetWithButtonPressed() {
  // reset();
  // }

  public void playPie(ActionEvent e) {
    // ci sto ancora lavorando
  }

  public void exitGame() {
    Platform.exit();
  }

  public Game getGame() {
    return game;
  }
}
