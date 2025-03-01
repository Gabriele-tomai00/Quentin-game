package quentin.network;

import java.util.Random;
import quentin.SettingHandler;

public class Server {

    private UDPServer udpServer;
    private TCPServer tcpServer;
    private String codeForClientAuth;
    private String username;
    private int tcpPort;

    public Server() {
        codeForClientAuth = generateRandomCode();
        setNetworkInfo();
    }

    public static void setNetworkInfo() {
        SettingHandler settingHandler = new SettingHandler();
        username = settingHandler.getUsername();
        tcpPort = settingHandler.getPort();
    }

    public void start() {
        udpServer = new UDPServer(username, tcpPort);
        udpServer.startServer();
        tcpServer = new TCPServer(tcpPort, codeForClientAuth);
        tcpServer.start();
    }

    public void stop() {
        if (udpServer == null) {
            System.out.println("UDP server not running");
            return;
        }
        udpServer.stopServer();
        tcpServer.stop();
    }

    public String generateRandomCode() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(10);
            randomNumbers.append(randomNumber);
        }
        return randomNumbers.toString();
    }

    public void sendMessage(String message) {
        tcpServer.sendMessage(message);
    }

    public String getCodeForClientAuth() {
        return codeForClientAuth;
    }

    public Boolean isClientAuth() {
        if (tcpServer == null) {
            return false;
        }
        return tcpServer.getClientAuth();
    }

    public String getBoardReceived() {
        if (tcpServer == null) {
            return null;
        }
        return tcpServer.getMessageReceived();
    }
}
