package quentin.network;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.Callable;

public class Server implements Callable<Socket> {

    private final SettingHandler handler;

    public Server(SettingHandler handler) {
        this.handler = handler;
    }

    @Override
    public Socket call() throws IOException {
        UdpServer udpServer = new UdpServer(handler.getUsername(), handler.getPort());
        udpServer.call();
        TcpServer tcpServer =
                new TcpServer(handler.getPort(), generateRandomCode(System.currentTimeMillis()));
        Socket socket;
        if ((socket = tcpServer.call()) == null) {
            throw new SocketException("Socket is null!");
        }
        return socket;
    }

    public static String generateRandomCode(long seed) {
        Random random = new Random(seed);
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(10);
            randomNumbers.append(randomNumber);
        }
        return randomNumbers.toString();
    }
}
