package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    private final String password;
    private final int port;

    public TcpServer(int port, String password) {
        this.port = port;
        this.password = password;
    }

    public Socket start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("CODE: " + password);
            String message;
            for (int attempt = 0; attempt < 3; attempt++) {
                message = in.readLine();
                if (message != null) {
                    if (!message.contains(password)) {
                        //            System.out.println("Invalid password, retry (attempt " +
                        // (attempt + 1) + "/3)");
                        out.println("ACCESS DENIED");
                    } else {
                        System.out.println("PASS OK");
                        out.println("SERVER OK");
                        return socket;
                    }
                }
            }
            out.println("SERVER ERR");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
