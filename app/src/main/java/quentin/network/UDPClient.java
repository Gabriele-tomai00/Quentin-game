package quentin.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class UDPClient {
    private static final int UDP_SERVER_PORT = 9876;
    private boolean discovery = true;
    private final String clientAddress;

    public UDPClient() {
        clientAddress = CorrectAddressGetter.getLocalIpAddress();
    }

    public ServerInfo call() {
        System.out.println("address of udp client: " + clientAddress);
        try {
            InetAddress localAddress = InetAddress.getByName(clientAddress);
            SocketAddress bindAddress = new InetSocketAddress(localAddress, 0);
            try (DatagramSocket clientSocket = new DatagramSocket(bindAddress)) {
                System.out.println("C: client discovery started using address: " + clientAddress);
                clientSocket.setBroadcast(true);
                return handleDiscovery(clientSocket);
            }
        } catch (Exception e) {
            System.err.println("Unexpected error in udp client discovery");
        }
        return null;
    }

    private ServerInfo handleDiscovery(DatagramSocket clientSocket) throws InterruptedException {
        byte[] sendBuffer = "Requesting server information".getBytes();
        byte[] receiveBuffer = new byte[1024];
        while (discovery) {
            try {
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                DatagramPacket sendPacket =
                        new DatagramPacket(
                                sendBuffer, sendBuffer.length, broadcastAddress, UDP_SERVER_PORT);
                clientSocket.send(sendPacket);

                System.out.println(
                        "Broadcast request sent (to: "
                                + broadcastAddress
                                + " port: "
                                + UDP_SERVER_PORT
                                + ")");

                DatagramPacket receivePacket =
                        new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.setSoTimeout(1000);
                clientSocket.receive(receivePacket);

                return ServerInfo.fromBytes(receivePacket.getData());
            } catch (SocketTimeoutException e) {
                // do nothing
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void stopDiscovery() {
        discovery = false;
    }
}
