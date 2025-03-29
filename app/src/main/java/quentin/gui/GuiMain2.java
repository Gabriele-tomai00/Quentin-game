package quentin.gui;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quentin.network.NetworkHandler;
import quentin.network.NetworkStarter;

public class GuiMain2 extends Application {
    private Parent root;

    public static void main(String... args) {
        launch();
    }

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
                        NetworkHandler handler = new NetworkHandler(socket, game);
                        try {
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            executor.submit(handler);
                            FXMLLoader loader =
                                    new FXMLLoader(getClass().getResource("Main2.fxml"));
                            loader.setController(new OnlineController(handler, game));
                            root = loader.load();
                            alreadyStarted();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
        starter.run();
    }
}
