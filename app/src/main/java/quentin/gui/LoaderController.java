package quentin.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import quentin.cache.Cache;
import quentin.cache.GameLog;

public class LoaderController implements Initializable {

    @FXML private ButtonBar bar;
    @FXML private Label label;
    private Controller controller;
    private Parent root;
    private Cache<GameLog> cache;

    public LoaderController(Cache<GameLog> cache) {
        this.cache = cache;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button yesButton = new Button("Yes");
        yesButton.addEventHandler(ActionEvent.ACTION, this::loadGame);
        Button noButton = new Button("No");
        noButton.addEventFilter(ActionEvent.ACTION, this::newGame);
        ButtonBar.setButtonData(yesButton, ButtonData.YES);
        ButtonBar.setButtonData(noButton, ButtonData.NO);
        bar.getButtons().addAll(yesButton, noButton);
        label.setText(
                String.format(
                        "Found saved game: %s%nWant to resume game?", cache.getLog().timeStamp()));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            loader.setController(new Controller(cache));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(ActionEvent e) {
        controller.start();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        setScene(stage);
    }

    public void newGame(ActionEvent e) {
        controller.reset();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        setScene(stage);
    }

    public void setScene(Stage primaryStage) {
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Quentin");
        primaryStage.setMinWidth(556);
        primaryStage.setMinHeight(516);
        primaryStage.show();
    }
}
