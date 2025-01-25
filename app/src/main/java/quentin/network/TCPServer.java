package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer implements TcpCliSerInterface {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String PASSWORD;
    private Boolean isClientAuth = false;
    private int port;
    private String messageReceived;
    private Thread waitMessageThread;

    public TCPServer(int port1, String pwd) throws IOException {
        port = port1;
        PASSWORD = pwd;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Error during socket creation in TCP Server");
            return;
        }
        System.out.println("TCP Server started on port " + port + ", waiting for client...");
        try {
            clientSocket = serverSocket.accept(); // Blocking call: waits for a client to connect
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Client authenticated: " + clientSocket.getInetAddress());
            System.out.println("CODE: " + PASSWORD);
            String message;
            for (int attempt = 0; attempt < 3; attempt++) {
                if ((message = in.readLine()) != null) {
                    System.out.println("Received first message: " + message);

                    if (!message.equals(PASSWORD)) {
                        System.out.println(
                                "Invalid password, retry (attempt " + (attempt + 1) + "/3)");
                        out.println("Invalid password, retry (attempt " + (attempt + 1) + "/3)");
                    } else {
                        System.out.println("Password of TCP client accepted");
                        isClientAuth = true;
                        out.println("Password accepted from TCP server");
                        listenForMessages();
                        return;
                    }
                } else {
                    System.out.println("Client connection interrupted");
                    stop();
                    return;
                }
            }
            System.out.println("too many authentication attempts with TCP server, exiting");
            out.println("too many authentication attempts with TCP server, exiting");
            stop();
        } catch (SocketException e) {
        } catch (IOException e) {
            System.err.println("IOException: problems with client connection");
        }
    }

    public void listenForMessages() {
        waitMessageThread =
                new Thread(
                        () -> {
                            try {
                                String serverMessage;
                                while ((serverMessage = in.readLine()) != null) {
                                    if (isClientAuth) {
                                        messageReceived = serverMessage;
                                        if (messageReceived.equals("Client stopped")) {
                                            stop();
                                        }
                                    } else
                                        System.out.println(
                                                "Can't receive from client because It's not auth");
                                }
                                System.out.println("client connection interrupted");
                                stop();
                            } catch (IOException e) {
                                System.out.println("IOException in listenForMessages");
                            }
                        });
        waitMessageThread.start();
    }

    public void sendMessage(String message) {
        if (isClientAuth) {
            out.println(message);
        }
    }

    public void stop() {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            //
        }
        try {
            waitMessageThread.join();
        } catch (Exception e) {
            //
        }
        System.out.println("TCP server successfully stopped");
    }

    public Boolean getClientAuth() {
        return isClientAuth;
    }

    public String getMessageReceived() {
        return messageReceived;
    }
}
