package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;
import quentin.exceptions.PasswordRejectedException;

public class TcpClient implements Callable<Socket> {
    private Socket socket;
    Scanner scanner;

    public TcpClient(Socket socket) {
        this.socket = socket;
        scanner = new Scanner(System.in);
    }

    @Override
    public Socket call() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            System.out.println("password > ");
            String password = scanner.nextLine();
            if (password.equals("exit")) {
                return null;
            }
            out.println(password);

            CommunicationProtocol message = CommunicationProtocol.fromString(in.readLine());

            if (message.equals(CommunicationProtocol.passOk())) {
                return socket;
            } else if (message.equals(CommunicationProtocol.serverErr())) {
                throw new PasswordRejectedException();
            } else {
                System.out.println(message);
            }
        }
    }
}
