package quentin;

import java.net.URL;
import java.util.ResourceBundle;

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
import quentin.cache.Cache;
import quentin.exceptions.MoveException;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.GameStarter;
import quentin.game.LocalGame;

public class Controller implements Initializable, GameStarter {

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
  private final Cache<LocalGame> cache;

  public Controller() {
    super();
    this.panes = new Pane[13][13];
    this.game = new LocalGame();
    cache = new Cache<>();
  }

  public Controller(Cache<LocalGame> cache) {
    super();
    panes = new Pane[13][13];
    game = cache.getLog();
    this.cache = cache;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    for (int i = 0; i < 13; i++) {
      for (int j = 0; j < 13; j++) {
        panes[i][j] = new Pane();
        panes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, this::placeCell);
        panes[i][j].setStyle("-fx-border-color: grey");
        // pane.setPrefSize(30, 30);
        board.add(panes[i][j], j, i);
      }
    }
    messageField.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startWithMouseClick);
  }

  @Override
  public void displayMessage(String message) {
    textField.setText(message);
  }

  @Override
  public void display() {
    BoardPoint[][] board = game.getBoard()
                               .getBoard();
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

  @Override
  public void startDisplay() {
    displayMessage(game.getCurrentPlayer() + "'s turn!");
    messageField.toBack();
    base.setEffect(null);
    base.setOpacity(1);
    base.toFront();
    display();
  }

  public void startWithMouseClick(MouseEvent e) {
    startDisplay();
  }

  @Override
  public void start() {
  }

  @Override
  public void displayWinner() {
    messageField.setText(new String(game.getCurrentPlayer() + " wins").toUpperCase());
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
      if (game.hasWon(game.getCurrentPlayer())) { displayWinner(); }
      game.changeCurrentPlayer();
      if (game.hasWon(game.getCurrentPlayer())) { displayWinner(); }
      cache.saveLog(new LocalGame(game));
      displayMessage(game.getCurrentPlayer() + "'s turn!");
    } catch (MoveException e1) {
      displayMessage(e1.getMessage());
    }
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

  public void resetWithMouseClicked(Object e) {
    reset();
  }

  public void goBack(ActionEvent e) {
    try {
      game = new LocalGame(cache.goBack());
      displayMessage(game.getCurrentPlayer() + "'s turn!");
    } catch (RuntimeException ex) {
      displayMessage(ex.getMessage());
    }
    display();
  }

  public void goForward(ActionEvent e) {
    try {
      game = new LocalGame(cache.goForward());
      displayMessage(game.getCurrentPlayer() + "'s turn!");
    } catch (RuntimeException ex) {
      displayMessage(ex.getMessage());
    }
    display();
  }

  public void exitGame(ActionEvent e) {
    Platform.exit();
  }

  public Cache<LocalGame> getCache() {
    return cache;
  }

}
