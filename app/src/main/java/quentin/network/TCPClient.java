package quentin.network;

import java.io.*;
import java.net.*;

enum State {
    notYetAuthenticated,
    authenticated,
    failedAuthentication,
}

public class TCPClient implements TCPclientServerInterface {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String boardReceived; // board
    private final int port;
    private final String address;
    private final String serverUsername;
    private Boolean clientConnected = false;
    private State state = State.notYetAuthenticated;

    public TCPClient(ServerInfo info) {
        address = info.IpAddress();
        serverUsername = info.username();
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
        if (clientConnected) out.println(message);
    }

    public void listenForMessages() {
        if (clientConnected) {
            new Thread(
                            () -> {
                                try {
                                    String serverMessage;
                                    while ((serverMessage = in.readLine()) != null) {
                                        //
                                        // System.out.println("Received from server: " +
                                        // serverMessage);

                                        if (state != State.authenticated) {
                                            if (serverMessage.equals(
                                                    "Password accepted from TCP server")) {
                                                state = State.authenticated;

                                            } else if (serverMessage.equals("server closed")) {
                                                state = State.failedAuthentication;
                                                stop();
                                            } else if (serverMessage.startsWith("Invalid password"))
                                                state = State.failedAuthentication;
                                        } else boardReceived = serverMessage;
                                    }
                                    System.out.println("server connection interrupted");
                                    stop();
                                } catch (IOException e) {
                                    // here when I call close() after a client connection
                                }
                            })
                    .start();
        } else System.out.println("Can't receive from server because It's not authenticated");
    }

    public void stop() {
        out.println("client closed");
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("Client process closed");
        } catch (IOException e) {
            //
        }
    }

    public String getServerUsername() {
        return serverUsername;
    }

    public String getBoardReceived() {
        return boardReceived;
    }

    public State getState() {
        return state;
    }
}
