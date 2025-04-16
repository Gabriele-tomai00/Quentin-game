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
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            while (true) {
                CommunicationProtocol received = CommunicationProtocol.fromString(br.readLine());
                System.out.println("Please make your move");
                synchronized (getGame()) {
                    switch (received.getType()) {
                        case EXIT -> {
                            exit();
                            System.out.println("The other player left the game!");
                            return;
                        }
                        case PIE -> {
                            getGame().applyPieRule();
                            System.out.println(
                                    "The other played used the pie rule\nYou are now: "
                                            + getGame().getCurrentPlayer());
                        }
                        case WINNER -> {
                            System.out.println(getGame().getBoard());
                            System.out.println("YOU " + received.getData() + " THE MATCH!!!!!");
                            return;
                        }
                        case MOVE -> {
                            Cell cell = new MoveParser(received.getData()).parse();
                            getGame().opponentPlaces(cell);
                            getGame().opponentCoversTerritories(cell);
                            System.out.println(getGame().getBoard());
                        }
                        case CHANGE -> System.out.println("The opponent cannot make a move!!!");
                        default -> { // something wrong happened
                        }
                    }
                    waiting = false;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public synchronized void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public synchronized boolean isWaiting() {
        return waiting;
    }

    public synchronized void sendCommands(CommunicationProtocol command) {
        if (!isWaiting()) {
            try {
                PrintWriter pw = new PrintWriter(getSocket().getOutputStream(), true);
                pw.println(command);
                waiting = true;
            } catch (IOException e) {
                System.err.println("error while sending the message: " + e.getMessage());
            }
        }
    }

    public synchronized void exit() {
        try {
            PrintWriter pw = new PrintWriter(getSocket().getOutputStream(), true);
            pw.println(CommunicationProtocol.exit());
            waiting = true;
        } catch (IOException e) {
            System.err.println("error during exit: " + e.getMessage());
        }
    }

    public synchronized void winner(String message) {
        try {
            PrintWriter pw = new PrintWriter(getSocket().getOutputStream(), true);
            pw.println(CommunicationProtocol.winner(message));
            waiting = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public OnlineGame getGame() {
        return game;
    }
}
