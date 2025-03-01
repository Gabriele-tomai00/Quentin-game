package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient implements TcpCliSerInterface {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String boardReceived; // board
    private final int port;
    private final String address;
    private Boolean clientConnected = false;
    private ClientAuthState authState = ClientAuthState.NOT_YET_AUTHENTICATED;

    public TCPClient(ServerInfo info) {
        address = info.IpAddress();
        port = info.Port();
    }

    public void start() {
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(
                    "TCP Client correctly authenticated, now you must send the password");
            clientConnected = true;
        } catch (IOException e) {
            System.err.println("Error initializing the client connection: " + e.getMessage());
            stop();
        }
    }

    public void sendMessage(String message) {
        if (clientConnected) {
            out.println(message);
        }
    }

    public void listenForMessages() {
        if (clientConnected) {
            new Thread(
                            () -> {
                                try {
                                    String serverMessage;
                                    while ((serverMessage = in.readLine()) != null) {
                                        if (authState != ClientAuthState.AUTHENTICATED) {
                                            if (serverMessage.equals(
                                                    "Password accepted from TCP server")) {
                                                authState = ClientAuthState.AUTHENTICATED;

                                            } else if (serverMessage.equals("server closed")) {
                                                authState = ClientAuthState.FAILED_AUTHENTICATION;
                                                stop();
                                            } else if (serverMessage.startsWith(
                                                    "Invalid password")) {
                                                authState = ClientAuthState.FAILED_AUTHENTICATION;
                                            }
                                        } else {
                                            boardReceived = serverMessage;
                                        }
                                    }
                                    System.out.println("server connection interrupted");
                                    stop();
                                } catch (IOException e) {
                                    if (clientConnected) {
                                        System.out.println("IOException error in TCP client");
                                    }
                                }
                            })
                    .start();
        } else {
            System.out.println("Can't receive from server because It's not authenticated");
        }
    }

    public void stop() {
        if (clientConnected) {
            clientConnected = false;
            out.println("quit");
            try {
                if (in != null) {
                    in.close();
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
                System.out.println("Client process closed");
            } catch (IOException e) {
                System.out.println("Error closing the client connection: " + e.getMessage());
            }
        }
    }

    public String getBoardReceived() {
        return boardReceived;
    }

    public ClientAuthState getState() {
        return authState;
    }
}
