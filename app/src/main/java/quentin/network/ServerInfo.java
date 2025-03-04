package quentin.network;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public record ServerInfo(InetAddress IpAddress, int Port, String username) {

    public static ServerInfo fromString(String string) {
        String[] parts = string.split(" - ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid string format: " + string);
        }
        return new ServerInfo(
                InetAddress.ofLiteral(parts[0]), Integer.parseInt(parts[1]), parts[2]);
    }

    public byte[] toBytes() {
        return this.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static ServerInfo fromBytes(byte[] bytes) {
        String string = new String(bytes, StandardCharsets.UTF_8);
        return fromString(string);
    }

    @Override
    public String toString() {
        return String.format("%s - %d - %s", IpAddress, Port, username);
    }
}
