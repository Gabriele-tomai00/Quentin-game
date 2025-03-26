package quentin.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import quentin.game.BoardPoint;
import quentin.game.Cell;
import quentin.game.MoveParser;
import quentin.game.OnlineGame;

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
            CommunicationProtocol received;
            while ((received = CommunicationProtocol.fromString(br.readLine())) != null) {
                System.out.println("Please make your move");
                synchronized (game) {
                    switch (received.getType()) {
                        case EXIT -> {
                            System.out.println("The other player left the game!");
                            return;
                        }
                        case PIE -> {
                            game.applyPieRule();
                            System.out.println(
                                    "The other played used the pie rule\nYou are now: "
                                            + game.getCurrentPlayer());
                        }
                        case WINNER -> {
                            System.out.println(game.getBoard());
                            System.out.println("YOU " + received.getData() + " THE MATCH!!!!!");
                            return;
                        }
                        default -> {
                            if (received.getType() == MessageType.MOVE) {
                                Cell cell = new MoveParser(received.getData()).parse();
                                game.opponentPlaces(cell);
                                game.opponentCoversTerritories(cell);
                                System.out.println(game.getBoard());
                            }
                        }
                    }
                    waiting = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public synchronized boolean isWaiting() {
        return waiting;
    }

    public synchronized void sendCommands(CommunicationProtocol command) {
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
