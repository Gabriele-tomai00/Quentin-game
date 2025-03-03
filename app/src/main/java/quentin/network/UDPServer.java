package quentin.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPServer {
    private static final int UDP_SERVER_PORT = 9876;
    private volatile boolean discovery = true; // Use volatile for thread-safe visibility
    private final String username;
    private final int tcpPort;
    private final String address;

    public UDPServer(String user, int tcpPortToComunicate) {
        username = user;
        tcpPort = tcpPortToComunicate;
        address = CorrectAddressGetter.getLocalIpAddress();
    }

    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket(UDP_SERVER_PORT)) {
            System.out.println(
                    "UDP Server is listening: ip " + address + " port " + UDP_SERVER_PORT);
            discovery = true;
            serverSocket.setSoTimeout(1000);
            receiveAndRespondUdp(serverSocket);
        } catch (Exception e) {
            System.err.println("Error during server socket initialization in UDP server");
        }
    }

    public void receiveAndRespondUdp(DatagramSocket serverSocket) {
        byte[] receiveBuffer = new byte[1024];
        while (discovery) {
            try {
                DatagramPacket receivePacket =
                        new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                System.out.println(
                        "UDP: Received request from client " + clientAddress + ":" + clientPort);
                ServerInfo serverInfo = new ServerInfo(address, tcpPort, username);

                byte[] sendBuffer = serverInfo.toBytes();

                DatagramPacket sendPacket =
                        new DatagramPacket(
                                sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);

                System.out.println(
                        "UDP: Response sent to client: "
                                + serverInfo
                                + " clientAddress: "
                                + clientAddress);
            } catch (SocketTimeoutException e) {
                // do nothing
            } catch (Exception e) {
                System.err.println("Unexpected error occurred in UDP server");
            }
        }
    }

    public void stopServer() {
        discovery = false;
    }
}
