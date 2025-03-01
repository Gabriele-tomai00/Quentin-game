package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer implements TcpCliSerInterface {
    private final String password;
    private final int port;
    
    public TCPServer(int port, String password) {
        this.port = port;
        this.password = password;
    }

    public Socket start() {
        try (serverSocket = new ServerSocket(port)){
            socket = serverSocket.accept(); // Blocking call: waits for a client to connect
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("CODE: " + password);
            String message;
            for (int attempt = 0; attempt < 3; attempt++) {
                if ((message = in.readLine()) != null) {
                    if (!message.equals(password)) {
                        System.out.println(
                                "Invalid password, retry (attempt " + (attempt + 1) + "/3)");
                        out.println("Invalid password, retry (attempt " + (attempt + 1) + "/3)");
                    } else {
                        System.out.println("Password of TCP client accepted");
                        out.println("Password accepted from TCP server");
                        return socket;
                    }}}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
