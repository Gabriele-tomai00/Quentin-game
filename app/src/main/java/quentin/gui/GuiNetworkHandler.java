package quentin.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import quentin.game.Cell;
import quentin.game.MoveParser;
import quentin.network.CommunicationProtocol;
import quentin.network.MessageType;
import quentin.network.NetworkHandler;

public class GuiNetworkHandler extends NetworkHandler {

    private OnlineGuiGame game;

    public GuiNetworkHandler(Socket socket, OnlineGuiGame game) {
        super(socket, game);
        this.game = game;
    }

    @Override
    public void run() {
        try {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            while (true) {
                CommunicationProtocol received = CommunicationProtocol.fromString(br.readLine());
                synchronized (getGame()) {
                    switch (received.getType()) {
                        case EXIT -> {
                            return;
                        }
                        case PIE -> getGame().applyPieRule();
                        case WINNER -> getGame().setSomeoneWon(received.getData());
                        default -> {
                            if (received.getType() == MessageType.MOVE) {
                                Cell cell = new MoveParser(received.getData()).parse();
                                game.opponentPlaces(cell);
                                game.opponentCoversTerritories(cell);
                            }
                        }
                    }
                    setWaiting(false);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public OnlineGuiGame getGame() {
        return game;
    }
}
