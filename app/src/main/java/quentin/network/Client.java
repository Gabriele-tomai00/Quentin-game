package quentin.network;

public class Client {
    private UDPClient udpClient;
    private TCPClient tcpClient;

    public void startDiscovery() {
        udpClient = new UDPClient();
        udpClient.setOnDiscoveredCallback(this::linkWithTCPServer);
        udpClient.startDiscovery(); // non-blocking
    }

    public void stopDiscovery() {
        udpClient.stopDiscovery();
    }

    public void stop() {
        udpClient.stopDiscovery();
        tcpClient.stop();
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

    public void sendAuthentication(String code) {
        tcpClient.sendMessage(code);
        System.out.println("Password sent to server: " + code);
    }

    public void sendMessage(String message) {
        tcpClient.sendMessage(message);
    }

    public String getBoardReceived() {
        if (tcpClient == null) return null;
        return tcpClient.getBoardReceived();
    }

    public ClientAuthState getStateAuthentication() {
        return tcpClient.getState();
    }

    public Boolean isAuthenticated() {
        return tcpClient.getState() == ClientAuthState.AUTHENTICATED;
    }
}
