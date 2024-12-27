package quentin.network;

import java.net.*;

public class UDPServer {
    private static final int UDP_SERVER_PORT = 9876;
    private volatile boolean discovery = true; // Use volatile for thread-safe visibility
    private Thread discoveryThread;
    private final String username;
    private final int tcpPort;
    private final String address;

    public UDPServer(String user, int tcpPortToComunicate) {
        username = user;
        tcpPort = tcpPortToComunicate;
        address = getCorrectAddress.getLocalIpAddress();
    }

    public void startServer() {
        discoveryThread =
                new Thread(
                        () -> {
                            try (DatagramSocket serverSocket =
                                    new DatagramSocket(UDP_SERVER_PORT)) {
                                System.out.println(
                                        "UDP Server is listening: ip "
                                                + address
                                                + " port "
                                                + UDP_SERVER_PORT);
                                byte[] receiveBuffer = new byte[1024];
                                discovery = true;
                                serverSocket.setSoTimeout(
                                        1000); // Set timeout of 1 second for when I call stop
                                // server

                                while (discovery) {
                                    try {
                                        // Blocking call: waits for an incoming packet from a client
                                        DatagramPacket receivePacket =
                                                new DatagramPacket(
                                                        receiveBuffer, receiveBuffer.length);
                                        serverSocket.receive(receivePacket);

                                        InetAddress clientAddress = receivePacket.getAddress();
                                        int clientPort = receivePacket.getPort();

                                        System.out.println(
                                                "UDP: Received request from client "
                                                        + clientAddress
                                                        + ":"
                                                        + clientPort);

                                        // Prepare the response message
                                        ServerInfo serverInfo =
                                                new ServerInfo(address, tcpPort, username);

                                        byte[] sendBuffer = serverInfo.toBytes();

                                        // Send the response to the client
                                        DatagramPacket sendPacket =
                                                new DatagramPacket(
                                                        sendBuffer,
                                                        sendBuffer.length,
                                                        clientAddress,
                                                        clientPort);
                                        serverSocket.send(sendPacket);

                                        System.out.println(
                                                "UDP: Response sent to client: "
                                                        + serverInfo
                                                        + " clientAddress: "
                                                        + clientAddress);
                                    } catch (SocketTimeoutException e) {
                                        // Timeout occurred: Check if we need to stop
                                        if (!discovery) {
                                            System.out.println(
                                                    "Timeout occurred, stopping server.");
                                        }
                                    } catch (Exception e) {
                                        System.err.println(
                                                "Unexpected error occurred in UDP server");
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println(
                                        "Error during server socket initialization in UDP server");
                            }
                        });
        discoveryThread.start();
    }

    public void stopServer() {
        discovery = false;
        try {
            discoveryThread.join(); // Wait for the thread to terminate
            if (!discoveryThread.isAlive()) {
                System.out.println("UDP server successfully stopped");
            }
        } catch (Exception e) {
            System.err.println("Error stopping UDP server");
        }
    }
}
