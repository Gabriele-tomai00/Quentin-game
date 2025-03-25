package quentin.network;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Callable;

public class Client implements Callable<Socket> {

    private final SettingHandler handler;

    public Client(SettingHandler handler) {
        this.handler = handler;
    }

    public Socket call() throws IOException {
        UdpClient udpClient = new UdpClient(handler.getUsername(), handler.getPort());
        NetworkInfo info = udpClient.call();
        if (info == null) {
            throw new IOException("No UDP server reached!");
        }
        TcpClient tcpClient = new TcpClient(new Socket(info.address(), handler.getPort()));
        Socket socket;
        if ((socket = tcpClient.call()) == null) {
            throw new SocketException("Unable to create socket");
        }
        return socket;
    }
}
