package quentin.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quentin.cache.Cache;
import quentin.cache.CacheHandler;
import quentin.cache.GameLog;

public class GuiMain extends Application {
    private Cache<GameLog> cache;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (cache != null && cache.getMemorySize() > 0 && cache.getLog() instanceof GameLog) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Load.fxml"));
            fxmlLoader.setController(new LoaderController(cache));
            Parent root = fxmlLoader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Quentin");
            primaryStage.show();
        } else {
            cache = new Cache<>();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            loader.setController(new Controller(cache));
            Parent root = loader.load();
            primaryStage.setTitle("Quentin");
            primaryStage.setScene(new Scene(root));
            primaryStage.setMinWidth(556);
            primaryStage.setMinHeight(516);
            primaryStage.show();
        }
    }

    @Override
    public void init() throws Exception {
        cache = CacheHandler.initialize();
    }

    @Override
    public void stop() throws Exception {
        CacheHandler.saveCache(cache);
    }
}
