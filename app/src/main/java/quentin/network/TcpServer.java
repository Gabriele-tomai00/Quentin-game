package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

public class TcpServer implements Callable<Socket> {
    private final String password;
    private final int port;

    public TcpServer(int port, String password) {
        this.port = port;
        this.password = password;
    }

    @Override
    public Socket call() throws IOException {
        Socket socket = getConnection();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("CODE: " + password);
        String message;
        for (int attempt = 0; attempt < 3; attempt++) {
            message = in.readLine();

            if (message != null && message.contains(password)) {
                out.println(CommunicationProtocol.passOk());
                return socket;
            }
            out.println(CommunicationProtocol.wrongPass());
        }
        out.println(CommunicationProtocol.serverErr());
        return null;
    }

    public Socket getConnection() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return serverSocket.accept();
        }
    }
}
