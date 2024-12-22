package quentin.network;

import java.io.IOException;

public class Client {
    private UDPClient udpClient;
    private TCPClient tcpClient;

    public void startDiscovery() throws IOException {
        udpClient = new UDPClient();
        udpClient.setOnDiscoveredCallback(this::linkWithTCPServer);
        udpClient.startDiscovery(); // non-blocking
    }

    public void stopDiscovery() {
        udpClient.stopDiscovery();
    }

    public void linkWithTCPServer() {
        System.out.println("Linking with TCP server...");
        ServerInfo tcpServerInfo = udpClient.getTcpServerInfo();
        if (tcpServerInfo != null) {
            tcpClient = new TCPClient(tcpServerInfo);
            tcpClient.start();
            tcpClient.listenForMessages();
        } else {
            System.out.println("No TCP server information got");
        }
    }

    public void trySendAuthentication(String code) {
        if (code.matches("\\d{5}")) {
            tcpClient.sendMessage(code);
            System.out.println("Password sent to server: " + code);
        } else
            System.out.println(
                    "Invalid password. Please enter exactly 5 digits. Type again \"clienta\"");
    }

    public void sendMessage(String message) {
        tcpClient.sendMessage(message);
    }

    public Boolean getAuthenticated() {
        return tcpClient != null ? tcpClient.getAuthenticated() : false;
    }

    public String getMessageReceived() {
        if (tcpClient == null) return null;
        return tcpClient.getMessageReceived();
    }
}
