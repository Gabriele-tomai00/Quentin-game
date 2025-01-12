package quentin.game;

import java.io.Serializable;

public record Player(BoardPoint color) implements Serializable {
    @Override
    public String toString() {
        return switch (color) {
            case BLACK -> "Black player";
            default -> "White player";
        };
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 == this) {
            return true;
        }
        if (arg0 == null) {
            return false;
        }
        if (arg0 instanceof Player(BoardPoint color1)) {
            return color1 == this.color;
        }
        return false;
    }
}
