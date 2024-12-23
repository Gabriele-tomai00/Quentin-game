package quentin.network;

import java.io.*;
import java.net.*;

public class TCPServer implements TCPclientServerInterface {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String PASSWORD;
    public Boolean isClientAuth = false;
    public int port;
    public String messageReceived;
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
            System.out.println("Client connected: " + clientSocket.getInetAddress());
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
                }
            }
            System.out.println("too many authentication attempts with TCP server, exiting");
            out.println("too many authentication attempts with TCP server, exiting");
            stop();
        } catch (SocketException e) {
            // when close() is called, the function goes here
        } catch (IOException e) {
            System.err.println("IOException: problems with client connection");
        }
    }

    // Receives messages from the client (runs in a separate thread)
    public void listenForMessages() {
        // here when I call close() after a client connection
        waitMessageThread =
                new Thread(
                        () -> {
                            try {
                                String serverMessage;
                                while ((serverMessage = in.readLine()) != null) {
                                    if (isClientAuth) {
                                        System.out.println(
                                                "Received from client: " + serverMessage);
                                        messageReceived = serverMessage;
                                    } else
                                        System.out.println(
                                                "Can't receive from client because It's not auth");
                                }
                            } catch (IOException e) {
                                // here when I call close() after a client connection
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
}
