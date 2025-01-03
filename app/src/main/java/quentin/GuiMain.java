package quentin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quentin.cache.Cache;
import quentin.game.LocalGame;

public class GuiMain extends Application {
  private static final String GAME_DIR = System.getProperty("user.home") + "/.quentinGame";
  private static final String CACHE_FILE = GAME_DIR + "/last_match_cache.dat";
  private Cache<LocalGame> cache;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    if (cache != null && cache.getMemorySize() > 0) {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Load.fxml"));
      fxmlLoader.setController(new LoaderController(cache));
      Parent root = fxmlLoader.load();
      primaryStage.setScene(new Scene(root));
      primaryStage.setTitle("Quentin");
      primaryStage.show();
    } else {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
      loader.setController(new Controller());
      Parent root = loader.load();
      primaryStage.setTitle("Quentin");
      primaryStage.setScene(new Scene(root));
      primaryStage.setMinWidth(556);
      primaryStage.setMinHeight(516);
      primaryStage.show();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void init() throws Exception {
    if (new File(CACHE_FILE).exists()) {
      try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(CACHE_FILE)))) {
        cache = (Cache<LocalGame>) input.readObject();
        System.out.println(cache);
      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void stop() throws Exception {
    File cacheDirectory = new File(GAME_DIR);
    if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
      throw new RuntimeException("Failed to create cache directory" + GAME_DIR);
    }
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(CACHE_FILE)))) {
      oos.writeObject(cache);
      System.out.println(cache);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
