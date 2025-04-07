package quentin.network;

import java.util.Objects;
import quentin.game.Cell;

public class CommunicationProtocol {
    private final MessageType type;
    private final String data;

    public CommunicationProtocol(MessageType type) {
        this.type = type;
        data = "";
    }

    public CommunicationProtocol(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

    public CommunicationProtocol(Cell cell) {
        type = MessageType.MOVE;
        data = String.format("%c%d", cell.col() + 'a', cell.row() + 1);
    }

    public static CommunicationProtocol fromString(String rawMessage) {
        String[] parts = rawMessage.split(":", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid message format: " + rawMessage);
        }
        return new CommunicationProtocol(MessageType.fromString(parts[0]), parts[1]);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", type.getValue(), data);
    }

    public MessageType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public static CommunicationProtocol passOk() {
        return new CommunicationProtocol(MessageType.PASSWORD, "OK");
    }

    public static CommunicationProtocol wrongPass() {
        return new CommunicationProtocol(MessageType.PASSWORD, "WRONG");
    }

    public static CommunicationProtocol serverErr() {
        return new CommunicationProtocol(MessageType.SERVER, "ERR");
    }

    public static CommunicationProtocol exit() {
        return new CommunicationProtocol(MessageType.EXIT);
    }

    public static CommunicationProtocol pie() {
        return new CommunicationProtocol(MessageType.PIE);
    }

    public static CommunicationProtocol move(String move) {
        return new CommunicationProtocol(MessageType.MOVE, move);
    }

    public static CommunicationProtocol change() {
        return new CommunicationProtocol(MessageType.CHANGE);
    }

    public static CommunicationProtocol winner(String message) {
        return new CommunicationProtocol(MessageType.WINNER, message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return obj instanceof CommunicationProtocol mess
                && getType() == mess.getType()
                && getData().equals(mess.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getData());
    }
}
