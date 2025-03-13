package quentin.network;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import quentin.SettingHandler;

public class Client implements Callable<Socket> {

    private final SettingHandler handler;

    public Client() {
        handler = new SettingHandler();
    }

    public Socket call() throws IOException {
        UdpClient udpClient = new UdpClient(handler.getPort(), handler.getUsername());
        NetworkInfo info = udpClient.run();
        TcpClient tcpClient = new TcpClient(new Socket(info.address(), handler.getPort()));
        return tcpClient.start();
    }
}
