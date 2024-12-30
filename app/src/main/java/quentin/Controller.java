package quentin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
    @FXML private Button exitButton;
    private Background black = Background.fill(Color.BLACK);
    private Background white = Background.fill(Color.WHITE);
    private Stage stage;
    private Cache<LocalGame> cache;
    private static final String GAME_DIR = System.getProperty("user.home") + "/.quentinGame";
    private static final String CACHE_FILE = GAME_DIR + "/last_match_cache.dat";

    public Controller() {
        super();
        this.panes = new Pane[13][13];
        this.game = new LocalGame();
        cache = new Cache<>();
    }

    @SuppressWarnings("unchecked")
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
        if (new File(CACHE_FILE).exists()) {
            try (ObjectInputStream input =
                    new ObjectInputStream(new FileInputStream(new File(CACHE_FILE)))) {
                cache = (Cache<LocalGame>) input.readObject();
                if (cache != null) {
                    if (cache.getLog() instanceof LocalGame game) {
                        this.game = game;
                        askResumeGame();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void exitGame(ActionEvent e) {
        File cacheDirectory = new File(GAME_DIR);
        if (!cacheDirectory.exists() && !cacheDirectory.mkdir()) {
            throw new RuntimeException("Failed to create cache directory" + GAME_DIR);
        }
        try (ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(new File(CACHE_FILE)))) {
            oos.writeObject(cache);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    public void askResumeGame() {
        ButtonBar bar = new ButtonBar();
        Button yesButton = new Button("Yes");
        yesButton.addEventHandler(ActionEvent.ACTION, this::loadGame);
        Button noButton = new Button("No");
        noButton.addEventFilter(ActionEvent.ACTION, this::newGame);
        ButtonBar.setButtonData(yesButton, ButtonData.YES);
        ButtonBar.setButtonData(noButton, ButtonData.NO);
        bar.getButtons().addAll(yesButton, noButton);
        stage = new Stage();
        stage.setScene(new Scene(bar));
        stage.show();
    }

    public void loadGame(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        start();
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

    private void newGame(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        cache = new Cache<>();
        game = new LocalGame();
        start();
    }
}
