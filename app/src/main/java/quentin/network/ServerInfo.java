package quentin.network;

public record ServerInfo(String address, int port, String username) {

  public static ServerInfo fromString(String string) throws NumberFormatException {
    String[] parts = string.split(" - ");
    if (parts.length != 3) { throw new IllegalArgumentException("Invalid string format: " + string); }
    return new ServerInfo(parts[0], Integer.parseInt(parts[1]), parts[2]);
  }

  public byte[] toBytes() {
    return this.toString()
               .getBytes();
  }

  @Override
  public String toString() {
    return String.format("%s - %d - %s", address, port, username);
  }
}
