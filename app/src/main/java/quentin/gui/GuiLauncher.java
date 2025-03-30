package quentin.gui;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quentin.cache.Cache;
import quentin.cache.CacheHandler;
import quentin.cache.GameLog;

public class GuiLauncher extends Application {
    private Cache<GameLog> cache;
    private static final File CACHE_FILE = new File(".last_match_cache");

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (cache != null && cache.getMemorySize() > 0 && cache.getLog() != null) {
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
    public void init() {
        cache = CacheHandler.initialize(CACHE_FILE);
    }

    @Override
    public void stop() {
        CacheHandler.saveCache(CACHE_FILE, cache);
    }
}
