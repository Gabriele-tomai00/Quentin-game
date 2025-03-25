package quentin.network;

public record NetworkInfo(String address, String username) {

    public static NetworkInfo fromString(String string) throws NumberFormatException {
        String[] parts = string.split(" - ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid string format: " + string);
        }
        return new NetworkInfo(parts[0], parts[1]);
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", address, username);
    }
}
