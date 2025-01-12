package quentin.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

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
        address = CorrectAddressGetter.getLocalIpAddress();
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
                                serverSocket.setSoTimeout(1000);

                                while (discovery) {
                                    try {
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
                                        ServerInfo serverInfo =
                                                new ServerInfo(address, tcpPort, username);

                                        byte[] sendBuffer = serverInfo.toBytes();

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
        if (discovery) {
            discovery = false;
            try {
                discoveryThread.join();
                System.out.println("UDP server successfully stopped");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error stopping UDP server: Thread was interrupted");
            } catch (Exception e) {
                System.err.println("Unexpected error while stopping UDP server: " + e.getMessage());
            }
        }
    }
}
