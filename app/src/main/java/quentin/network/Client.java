package quentin.network;

import java.net.Socket;
import java.util.concurrent.Callable;
import quentin.SettingHandler;

public class Client implements Callable<Socket> {

    private final UdpClient client;
    private final String username;

    public Client() {
        client = new UdpClient();
        SettingHandler settingHandler = new SettingHandler();
        username = settingHandler.getUsername();
    }

    public Socket call() {
        TCPClient tcpClient = new TCPClient(client.run());
        return tcpClient.start();
    }

    public void stop() {
        client.stopDiscovery();
    }
}
