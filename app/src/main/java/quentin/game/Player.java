package quentin.game;

import java.io.Serializable;

public record Player(BoardPoint color) implements Serializable {

    public Player(BoardPoint color) {
        this.color = color;
    }

    @Override
    public final String toString() {
        return switch (color) {
            case BLACK -> "Black player";
            default -> "White player";
        };
    }

    @Override
    public final boolean equals(Object arg0) {
        if (arg0 == this) {
            return true;
        }
        if (arg0 == null) {
            return false;
        }
        if (arg0 instanceof Player player) {
            return player.color == this.color;
        }
        return false;
    }
}
