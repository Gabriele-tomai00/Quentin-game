package quentin.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class CorrectAddressGetter {

    public static String getLocalIpAddress() {
        NetworkInterface networkInterface = null;
        Enumeration<InetAddress> addresses = null;
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    interfaces.hasMoreElements(); ) {
                networkInterface = interfaces.nextElement();

                addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        return address.getHostAddress();
                    }
                }
            }
            return null;
        } catch (SocketException e) {
            System.err.println("Error in retrieving IP address");
            return null;
        }
    }
}
