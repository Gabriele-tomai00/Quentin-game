package quentin.network;

import java.util.Random;
import quentin.SettingHandler;
import java.net.Socket;

public class Server {

    private final UDPServer udpServer;
    private final TCPServer tcpServer;
    private final String codeForClientAuth;
    private final String username;
    private final int tcpPort;

    public Server() {
        codeForClientAuth = generateRandomCode();
        setNetworkInfo();
    }

    public void setNetworkInfo() {
        SettingHandler settingHandler = new SettingHandler();
        username = settingHandler.getUsername();
        tcpPort = settingHandler.getPort();
    }

    public Socket start() {
        udpServer = new UDPServer(username, tcpPort);
        udpServer.startServer();
        tcpServer = new TCPServer(tcpPort, codeForClientAuth);
        return tcpServer.start();
    }

    public static String generateRandomCode() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(10);
            randomNumbers.append(randomNumber);
        }
        return randomNumbers.toString();
    }
}
