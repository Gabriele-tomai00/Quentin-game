package quentin.network;

import java.net.*;

public class UDPClient {
    private static final int UDP_SERVER_PORT = 9876;
    private Boolean discovery = true;
    private Thread discoveryThread;
    private ServerInfo tcpServerInfo;
    private Runnable onDiscoveredCallback;
    private final String clientAddress;

    public UDPClient() {
        clientAddress = CorrectAddressGetter.getLocalIpAddress();
    }

    public ServerInfo getTcpServerInfo() {
        return tcpServerInfo;
    }

    public void startDiscovery() {
        System.out.println("address of udp client: " + clientAddress);
        discoveryThread =
                new Thread(
                        () -> {
                            try {
                                InetAddress localAddress = InetAddress.getByName(clientAddress);
                                SocketAddress bindAddress = new InetSocketAddress(localAddress, 0);
                                try (DatagramSocket clientSocket =
                                        new DatagramSocket(bindAddress)) {
                                    System.out.println(
                                            "C: client discovery started using address: "
                                                    + clientAddress);
                                    clientSocket.setBroadcast(true);
                                    handleDiscovery(clientSocket);
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.err.println(
                                        "Error stopping UDP client: Thread was interrupted");
                            } catch (Exception e) {
                                System.err.println("Unexpected error in udp client discovery");
                            }
                        });
        discoveryThread.start();
    }

    private void handleDiscovery(DatagramSocket clientSocket) throws InterruptedException {
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
                clientSocket.setSoTimeout(5000);
                clientSocket.receive(receivePacket);

                tcpServerInfo = ServerInfo.fromBytes(receivePacket.getData());
                System.out.println("Received ServerInfo: " + tcpServerInfo);
                if (onDiscoveredCallback != null) onDiscoveredCallback.run();

                break;
            } catch (Exception e) {
                Thread.sleep(400);
            }
        }
    }

    public void stopDiscovery() {
        discovery = false;
        try {
            discoveryThread.join();
            if (!discoveryThread.isAlive()) {
                System.out.println("client discovery correctly stopped");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error stopping udp client discovery");
        }
    }

    public void setOnDiscoveredCallback(Runnable callback) {
        this.onDiscoveredCallback = callback;
    }
}
