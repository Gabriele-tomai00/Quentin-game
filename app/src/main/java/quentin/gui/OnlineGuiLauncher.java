package quentin.gui;

import java.util.concurrent.ExecutorService;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        GuiNetworkStarter starter = new GuiNetworkStarter(executor);
        starter.run();
        root = starter.getRoot();
    }

    @Override
    public void stop() {
        executor.shutdownNow();
    }
}
