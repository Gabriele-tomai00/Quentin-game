package quentin.game;

public enum BoardPoint {
    BLACK,
    WHITE,
    EMPTY;

    @Override
    public String toString() {
        return switch (this) {
            case BLACK -> "B";
            case WHITE -> "W";
            default -> ".";
        };
    }
}
