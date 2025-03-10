package quentin.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import quentin.game.GameBoard;

public class NetworkHandler implements Runnable {

    private final Socket socket;
    private final OnlineGame game;
    private boolean waiting;

    public NetworkHandler(Socket socket, OnlineGame game) {
        super();
        this.socket = socket;
        this.game = game;
    }

    @Override
    public void run() {
        try (BufferedReader br =
                new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String received;
            while ((received = br.readLine()) != null) {
                synchronized (game) {
                    game.updateBoard(new GameBoard(received));
                    waiting = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isWaiting() {
        return waiting;
    }

    public synchronized void sendCommands(String command) {
        try (BufferedWriter bw =
                new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            if (!isWaiting()) {
                bw.write(command);
                waiting = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
