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

public class Controller {

    private final Pane[][] panes;
    private Game game;

    public Controller() {
        super();
        this.panes = new Pane[13][13];
        this.game = new Game();
    }

    @FXML private GridPane gridPane;
    @FXML private Label textField;
    @FXML private Label winnerText;

    @FXML private Button displayWinner;
    @FXML private Button reset;
    private Background black = Background.fill(Color.BLACK);
    private Background white = Background.fill(Color.WHITE);

    @FXML
    public void initialize() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                panes[i][j] = new Pane();
                panes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, this::placeCell);
                panes[i][j].setStyle("-fx-border-color: grey");
                // pane.setPrefSize(30, 30);
                gridPane.add(panes[i][j], j, i);
            }
        }
        displayMessage(game.getCurrentPlayer() + "'s turn!");
    }

    protected void displayMessage(Object obj) {
        textField.setText(obj instanceof String ? (String) obj : obj.toString());
    }

    protected void displayWinner() {
        winnerText.setText(new String(game.getCurrentPlayer() + " wins").toUpperCase());
        winnerText.toFront();
        gridPane.setEffect(new BoxBlur());
        gridPane.setOpacity(.5);
    }

    protected void display() {

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
            displayMessage(game.getCurrentPlayer() + "'s turn!");
        } catch (MoveException e1) {
            // TODO Auto-generated catch block
            displayMessage(e1.getMessage());
        }
    }

    public void reset(ActionEvent e) {
        game = new Game();
        winnerText.toBack();
        gridPane.setEffect(null);
        gridPane.setOpacity(1);
        display();
    }

    public void displayWinner(ActionEvent e) {
        displayWinner();
    }
}
