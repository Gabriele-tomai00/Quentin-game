package quentin.network;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.Callable;

public class UdpClient implements Callable<NetworkInfo> {
    private final int port;
    private final String username;

    public UdpClient(String username, int port) {
        this.port = port;
        this.username = username;
    }

    public NetworkInfo call() throws IOException {
        System.out.println("Looking for servers on port: " + port);
        while (!Thread.currentThread().isInterrupted()) {
            try (DatagramSocket clientSocket = createSocket()) {

                clientSocket.setBroadcast(true);
                String message = getLocalAddress() + " - " + username;
                byte[] buffer = message.getBytes();
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length, broadcastAddress, port);
                clientSocket.send(packet);

                buffer = new byte[1024];
                DatagramPacket recPacket = new DatagramPacket(buffer, buffer.length);
                clientSocket.setSoTimeout(2000);
                clientSocket.receive(recPacket);

                NetworkInfo info =
                        NetworkInfo.fromString(
                                new String(
                                                recPacket.getData(),
                                                recPacket.getOffset(),
                                                recPacket.getLength())
                                        .trim());
                System.out.println("Server found: " + info.username());
                return info;
            } catch (SocketTimeoutException e) {
                // do nothing
            }
        }
        return null;
    }

    protected DatagramSocket createSocket() throws SocketException {
        return new DatagramSocket();
    }

    private static String getLocalAddress() {
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
        return "IP non disponibile";
    }
}
