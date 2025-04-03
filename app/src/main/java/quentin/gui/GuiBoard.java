package quentin.gui;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
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

    @Serial private static final long serialVersionUID = 7808922781508489339L;
    private final transient List<List<ObjectProperty<Background>>> backgroundProperties;
    private static final int SIZE = 13;

    public GuiBoard() {
        super();
        backgroundProperties = new ArrayList<>(size());
        for (int row = 0; row < SIZE; row++) {
            backgroundProperties.add(row, new ArrayList<>(size()));
            for (int col = 0; col < SIZE; col++) {
                backgroundProperties
                        .get(row)
                        .add(
                                col,
                                new SimpleObjectProperty<>(
                                        new Background(
                                                new BackgroundFill(
                                                        null, CornerRadii.EMPTY, Insets.EMPTY))));
            }
        }
    }

    @Override
    public void placeStone(BoardPoint stone, int row, int col) {
        Color color = stone == BoardPoint.BLACK ? Color.BLACK : Color.WHITE;
        backgroundProperties
                .get(row)
                .get(col)
                .set(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        super.placeStone(stone, row, col);
    }

    public List<List<ObjectProperty<Background>>> getProperties() {
        return backgroundProperties;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
