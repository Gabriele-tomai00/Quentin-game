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

    public static BoardPoint fromString(String symbol) {
        return switch (symbol) {
            case "B", "Black" -> BoardPoint.BLACK;
            case "W", "White" -> BoardPoint.WHITE;
            default -> BoardPoint.EMPTY;
        };
    }
}
