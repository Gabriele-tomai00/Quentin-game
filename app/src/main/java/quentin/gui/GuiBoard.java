package quentin.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import quentin.game.Board;
import quentin.game.BoardPoint;

public class GuiBoard extends Board {

    private static final long serialVersionUID = 7808922781508489339L;
    private final transient ObjectProperty<Background>[][] backgroundProperties;
    private static final int SIZE = 13;

    @SuppressWarnings("unchecked")
    public GuiBoard() {
        backgroundProperties = new ObjectProperty[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                backgroundProperties[i][j] =
                        new SimpleObjectProperty<>(
                                new Background(
                                        new BackgroundFill(null, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    @Override
    public void placeStone(BoardPoint stone, int row, int col) {
        Color color = stone == BoardPoint.BLACK ? Color.BLACK : Color.WHITE;
        backgroundProperties[row][col].set(
                new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        super.placeStone(stone, row, col);
    }

    public ObjectProperty<Background>[][] getProperties() {
        return backgroundProperties;
    }
}
