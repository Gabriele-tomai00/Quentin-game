package quentin.network;

import java.io.IOException;
import java.util.Random;
import quentin.SettingHandler;

public class Server {

    private UDPServer udpServer;
    private TCPServer tcpServer;
    public String codeForClientAuth;
    private String username;
    private int tcpPort;

    public Server() {
        generateRandomCode();
        setNetworkInfo();
    }

    public void setNetworkInfo() {
        SettingHandler settingHandler = new SettingHandler();
        username = settingHandler.getUsername();
        tcpPort = settingHandler.getPort();
    }

    public void start() {
        udpServer = new UDPServer(username, tcpPort);
        udpServer.startServer();
        try {
            tcpServer = new TCPServer(tcpPort, codeForClientAuth);
        } catch (IOException e) {
            System.err.println("Error initializing the server connection");
            return;
        }
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

    public void generateRandomCode() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(10);
            randomNumbers.append(randomNumber);
        }
        codeForClientAuth = randomNumbers.toString();
    }

    public void sendMessage(String message) {
        tcpServer.sendMessage(message);
    }

    public String getCodeForClientAuth() {
        return codeForClientAuth;
    }

    public Boolean isClientAuth() {
        if (tcpServer == null) return false;
        return tcpServer.getClientAuth();
    }

    public String getMessageReceived() {
        if (tcpServer == null) return null;
        return tcpServer.getMessageReceived();
    }
}
