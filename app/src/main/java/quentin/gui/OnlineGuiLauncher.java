package quentin.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quentin.game.Cell;
import quentin.game.MoveParser;
import quentin.network.CommunicationProtocol;
import quentin.network.MessageType;
import quentin.network.NetworkHandler;
import quentin.network.NetworkStarter;

public class OnlineGuiLauncher extends Application {
    private Parent root;
    private ExecutorService executor;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Quentin");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(556);
        primaryStage.setMinHeight(516);
        primaryStage.show();
    }

    @Override
    public void init() {
        NetworkStarter starter =
                new NetworkStarter() {

                    @Override
                    protected void start(Socket socket) {
                        OnlineGuiGame game = new OnlineGuiGame(getColor());
                        NetworkHandler handler =
                                new NetworkHandler(socket, game) {
                                    @Override
                                    public void run() {
                                        try {
                                            BufferedReader br =
                                                    new BufferedReader(
                                                            new InputStreamReader(
                                                                    socket.getInputStream()));
                                            while (true) {
                                                CommunicationProtocol received =
                                                        CommunicationProtocol.fromString(
                                                                br.readLine());
                                                synchronized (game) {
                                                    switch (received.getType()) {
                                                        case EXIT -> {
                                                            return;
                                                        }
                                                        case PIE -> game.applyPieRule();
                                                        case WINNER ->
                                                                game.setSomeoneWon(
                                                                        received.getData());
                                                        default -> ifMove(game, received);
                                                    }
                                                    setWaiting(false);
                                                }
                                            }
                                        } catch (IOException e) {
                                            System.err.println(e.getMessage());
                                        }
                                    }
                                };
                        try {
                            executor = Executors.newSingleThreadExecutor();
                            executor.submit(handler);
                            FXMLLoader loader =
                                    new FXMLLoader(getClass().getResource("Main2.fxml"));
                            loader.setController(new OnlineController(handler, game));
                            root = loader.load();
                            alreadyStarted();
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                };
        starter.run();
    }

    public void ifMove(OnlineGuiGame game, CommunicationProtocol received) {
        if (received.getType() == MessageType.MOVE) {
            Cell cell = new MoveParser(received.getData()).parse();
            game.opponentPlaces(cell);
            game.opponentCoversTerritories(cell);
        }
    }

    @Override
    public void stop() {
        executor.shutdownNow();
    }
}
