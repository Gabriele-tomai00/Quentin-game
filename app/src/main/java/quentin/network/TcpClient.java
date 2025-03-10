package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {
    private boolean authenticating;
    private Socket socket;
    Scanner scanner;

    public TcpClient(Socket socket) {
        this.socket = socket;
        authenticating = true;
        scanner = new Scanner(System.in);
    }

    public Socket start() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (authenticating) {
                System.out.println("password > ");
                String password = scanner.nextLine().trim();
                if ((password.equals("exit"))) {
                    return null;
                }
                if ((password.length() != 5 || !password.matches("\\d{5}"))) {
                    System.out.println("Invalid password, retry");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
