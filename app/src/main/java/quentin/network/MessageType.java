package quentin.network;

public enum MessageType {
    EXIT("EXIT"),
    PASSWORD("PASS"),
    MOVE("MOVE"),
    PIE("PIE"),
    SERVER("SERVER"),
    WINNER("WINNER"),
    CHANGE("CHANGE");

    MessageType(String value) {
        this.value = value;
    }

    public static MessageType fromString(String message) {
        for (MessageType type : values()) {
            if (type.value.equals(message)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown message type: " + message);
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
