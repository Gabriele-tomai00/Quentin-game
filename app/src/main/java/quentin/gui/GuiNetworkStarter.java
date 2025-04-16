package quentin.gui;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import quentin.game.BoardPoint;
import quentin.network.NetworkHandler;
import quentin.network.NetworkStarter;

public class GuiNetworkStarter extends NetworkStarter {
    private final ExecutorService executor;
    private Parent root;

    public GuiNetworkStarter(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    protected void start(Socket socket, BoardPoint color) {
        OnlineGuiGame game = new OnlineGuiGame(color);
        NetworkHandler handler = new GuiNetworkHandler(socket, game);
        try {
            executor.submit(handler);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main2.fxml"));
            loader.setController(new OnlineController(handler, game));
            root = loader.load();
            alreadyStarted();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public Parent getRoot() {
        return root;
    }
}
