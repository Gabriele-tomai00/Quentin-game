package quentin;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Controller {

    private final Pane[][] panes;
    private final Game game;

    public Controller() {
        super();
        this.panes = new Pane[13][13];
        this.game = new Game();
    }

    @FXML public GridPane gridPane;
    private Background black = Background.fill(Color.BLACK);
    private Background white = Background.fill(Color.WHITE);

    @FXML
    public void initialize() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                panes[i][j] = new Pane();
                panes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, this::placeCell);
                panes[i][j].setStyle("-fx-border-color: black; -fx-background-color: lightblue;");
                // pane.setPrefSize(30, 30);
                gridPane.add(panes[i][j], j, i);
            }
        }
    }

    protected void startDisplay() {
        // TODO Auto-generated method stub

    }

    protected void displayMessage(String format) {}

    protected void displayWinner() {
        // TODO Auto-generated method stub

    }

    protected void display() {

        BoardPoint[][] board = game.getBoard().getBoard();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if (board[i][j] == BoardPoint.BLACK) {
                    panes[i][j].setBackground(black);
                } else if (board[i][j] == BoardPoint.WHITE) {
                    panes[i][j].setBackground(white);
                }
            }
        }
    }

    protected void setColor(MouseEvent e) {
        Pane source = (Pane) e.getSource();
        Integer rowIndex = GridPane.getRowIndex(source);
        Integer columnIndex = GridPane.getColumnIndex(source);
        panes[rowIndex][columnIndex].setBackground(Background.fill(Color.BLACK));
    }

    protected void placeCell(MouseEvent e) {
        Pane source = (Pane) e.getSource();
        Integer columnIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        Cell cell = new Cell(rowIndex.intValue(), columnIndex.intValue());
        try {
            game.place(cell);
            game.coverTerritories(cell);
            game.getBoard().hasWon(game.getCurrentPlayer().color());
            game.changeCurrentPlayer();
            game.getBoard().hasWon(game.getCurrentPlayer().color());
            display();
        } catch (MoveException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
