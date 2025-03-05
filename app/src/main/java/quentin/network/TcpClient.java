package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {
    private final int port;
    private Scanner scanner;
    private final InetAddress address;
    private boolean authenticating;

    public TcpClient(int port, InetAddress address) {
        this.address = address;
        this.port = port;
        authenticating = true;
    }

    public Socket start() {
        scanner = new Scanner(System.in);

        try {
            Socket socket = new Socket(address, port);
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                while (authenticating) {
                    System.out.println("password > ");
                    String password = scanner.nextLine().trim();
                    if ((password.equals("exit"))) {
                        return null;
                    }
                    if ((password.length() != 5 || !password.matches("\\d{5}"))) {
                        System.out.println("Invalid password, retry\n");
                    }
                    out.println(password);
                    String message = in.readLine();
                    if (message.equals("SERVER OK")) {
                        authenticating = false;
                    } else if (message.equals("SERVER ERR")) {
                        throw new RuntimeException("Password was inputted incorrectly");
                    }
                }
                return socket;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
