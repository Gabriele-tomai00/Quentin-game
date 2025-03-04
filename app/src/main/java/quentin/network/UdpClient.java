package quentin.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

public class UdpClient {
    private static final int UDP_SERVER_PORT = 9876;
    private boolean discovery = true;
    private final String clientAddress;

    public UdpClient() {
        clientAddress = CorrectAddressGetter.getLocalIpAddress();
    }

    public ServerInfo run() {
        System.out.println("address of udp client: " + clientAddress);
        try (DatagramSocket clientSocket =
                new DatagramSocket(
                        new InetSocketAddress(InetAddress.getByName(clientAddress), 0))) {
            System.out.println("C: client discovery started using address: " + clientAddress);
            clientSocket.setBroadcast(true);

            byte[] sendBuffer = "Requesting server information".getBytes();
            byte[] receiveBuffer = new byte[1024];
            while (discovery) {
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
            }
        } catch (SocketTimeoutException e) {
            // do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stopDiscovery() {
        discovery = false;
    }
}
