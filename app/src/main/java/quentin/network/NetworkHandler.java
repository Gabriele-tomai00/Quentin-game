package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import quentin.game.BoardPoint;

public class NetworkHandler implements Runnable {

    private final Socket socket;
    private final OnlineGame game;
    private boolean waiting;

    public NetworkHandler(Socket socket, OnlineGame game) {
        this.socket = socket;
        this.game = game;
        this.waiting = game.getCurrentPlayer().color() == BoardPoint.WHITE;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String received;
            while ((received = br.readLine()) != null) {
                System.out.println("Please make your move");
                synchronized (game) {
                    switch (received) {
                        case "exit" -> {
                            System.out.println("The other player left the game!");
                            return;
                        }
                        case "pie" -> {
                            game.applyPieRule();
                            System.out.println(
                                    "The other played used the pie rule\nYou are now: "
                                            + game.getCurrentPlayer());
                        }
                        default -> {
                            game.getBoard().fromCompactString(received);
                            System.out.println(game.getBoard());
                        }
                    }
                }
                waiting = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isWaiting() {
        return waiting;
    }

    public synchronized void sendCommands(String command) {
        if (!isWaiting()) {
            try {
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                pw.println(command);
                waiting = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
