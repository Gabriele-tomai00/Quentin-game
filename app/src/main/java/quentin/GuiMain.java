package quentin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiMain extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Quentin");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(556);
        primaryStage.setMinHeight(516);
        // primaryStage.setResizable(false);
        primaryStage.show();
    }
}
