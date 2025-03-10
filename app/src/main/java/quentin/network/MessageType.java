package quentin.network;

public enum MessageType {
    EXIT("exit"),
    PASS_OK("pass ok"),
    MOVE("move"),
    PIE("pie"),
    SERVER_ERR("server err");

    MessageType(String value) {
        this.value = value;
    }

    public static MessageType fromString(String message) {
        for (MessageType type : values()) {
            if (type.value.equals(message)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unkown message type: " + message);
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
