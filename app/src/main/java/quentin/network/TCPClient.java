package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    private final int port;
    private final InetAddress address;
    private ClientAuthState authState = ClientAuthState.NOT_YET_AUTHENTICATED;

    public TCPClient(ServerInfo info) {
        address = info.IpAddress();
        port = info.Port();
    }

    public Socket start() {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = new Socket(address, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //      System.out.println("TCP Client correctly authenticated, now you must send the
            // password");
            //      TODO add prompt for inserting password
            while (authState != ClientAuthState.AUTHENTICATED) {
                while (authState != ClientAuthState.AUTHENTICATED) {
                    System.out.println("password > ");
                    String password = scanner.nextLine().trim();
                    if ((password.equals("exit"))) {
                        return null;
                    }
                    if ((password.length() != 5 || !password.matches("\\d{5}"))) {
                        System.out.println("Invalid password, retry\n");
                    }
                    out.write(password);
                    if (in.readLine().equals("SERVER OK")) {
                        break;
                    }
                }
                String serverMessage = in.readLine();
                if (serverMessage.equals("Password accepted from TCP server")) {
                    authState = ClientAuthState.AUTHENTICATED;
                } else if (serverMessage.equals("server closed")) {
                    authState = ClientAuthState.FAILED_AUTHENTICATION;
                    throw new RuntimeException();
                }
            }
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
