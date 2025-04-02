package quentin.gui;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

@ExtendWith(ApplicationExtension.class)
class ControllerTest extends ApplicationTest {

    private Controller controller;
    private Label messageField;
    private Button clickButton;

    //    @BeforeAll
    //    static void setupHeadless() {
    //        if (Boolean.getBoolean("headless")) {
    //            System.setProperty("testfx.robot", "glass");
    //            System.setProperty("testfx.headless", "true");
    //            System.setProperty("glass.platform", "Monocle");
    //            System.setProperty("monocle.platform", "Headless");
    //            System.setProperty("prism.order", "sw");
    //            System.setProperty("prism.text", "t2k");
    //        }
    //        //    javafx.application.Platform.startup(() -> {});
    //    }
    //
    //    @Override
    //    public void start(Stage stage) throws Exception {
    //        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
    //        loader.setController(new Controller());
    //        Parent root = loader.load();
    //        stage.setScene(new Scene(root));
    //        stage.show();
    //        controller = loader.getController();
    //
    //        // Get UI elements from the controller
    //        messageField = lookup("#messageField").query();
    //        //    clickButton = lookup("#clickButton").query();
    //    }
    //
    //    //    @AfterEach
    //    //    void afterEach() throws Exception {
    //    //        FxToolkit.hideStage();
    //    //        release(new KeyCode[] {});
    //    //        release(new MouseButton[] {});
    //    //    }
//    
//        @Test
//        void shouldUpdateLabelWhenButtonClicked() {
//            messageField = lookup("#messageField").query();
//            // Ensure initial state
//            verifyThat(messageField, hasText("Hello, World!"));
//    
//            // Simulate button click
//            clickOn(clickButton);
//    
//            // Check if the text updated correctly
//            verifyThat(messageField, hasText("Button Clicked!"));
//        }
}
