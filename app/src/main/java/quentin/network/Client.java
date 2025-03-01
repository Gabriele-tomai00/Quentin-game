package quentin.network;

public class Client {
    private final UDPClient udpClient;
    private final TCPClient tcpClient;

    public Client() {
        SettingHandler settingHandler = new SettingHandler();
        username = settingHandler.getUsername();
    }

    public void startDiscovery() {
        udpClient = new UDPClient();
        tcplient = new TCPClient(udpClient.start());
        tcpClient.start();
    }

    public void stop() {
        udpClient.stopDiscovery();
    }
}
