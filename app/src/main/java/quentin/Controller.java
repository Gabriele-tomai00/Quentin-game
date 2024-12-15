package quentin;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Controller extends GameStarter<MouseEvent> {

    private final Pane[][] panes;
    private final Background white;
    private final Background black;

    public Controller() {
        super();
        panes = new Pane[13][13];
        white = Background.fill(Color.WHITE);
        black = Background.fill(Color.BLACK);
    }

    @FXML private GridPane gridPane;

    @FXML
    public void initialize() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                panes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, this::getInput);
                // pane.setStyle("-fx-border-color: black; -fx-background-color: lightblue;");
                // pane.setPrefSize(30, 30);
                gridPane.add(panes[i][j], i, j);
            }
        }
        start();
    }

    @Override
    protected void startDisplay() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void displayMessage(String format) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void displayWinner() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void display() {
        BoardPoint[][] board = game().getBoard().getBoard();
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

    @Override
    protected Cell getInput(MouseEvent e) {
        Pane source = (Pane) e.getSource();
        Integer columnIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        System.out.print(e);
        return new Cell(rowIndex.intValue(), columnIndex.intValue());
    }
}
