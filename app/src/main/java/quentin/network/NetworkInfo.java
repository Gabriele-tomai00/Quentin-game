package quentin.network;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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

    public static String getLocalAddress() {
        try {
            final Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                for (InterfaceAddress addr : interfaces.nextElement().getInterfaceAddresses()) {
                    if (addr.getAddress().isSiteLocalAddress()) {
                        return addr.getAddress().getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error: unable to get local IP address: " + e.getMessage());
        }
        return "IP not available";
    }
}
