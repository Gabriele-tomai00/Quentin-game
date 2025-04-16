package quentin.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;

@ExtendWith(ApplicationExtension.class)
class ControllerTest extends ApplicationTest {

    private Controller controller;
    private Label messageField;
    private Button resetButton;
    private Button goBackButton;
    private Button goForwardButton;

    @BeforeAll
    static void setupHeadless() {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("glass.platform", "Monocle");
            System.setProperty("monocle.platform", "Headless");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        loader.setController(new Controller());
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
        controller = loader.getController();
        messageField = lookup("#messageField").query();
        resetButton = lookup("#resetButton").query();
        goBackButton = lookup("#goBackButton").query();
        goForwardButton = lookup("#goForwardButton").query();
    }

    @Test
    void testInitialMessage() {
        assertEquals("Click anywhere to start!", messageField.getText());
    }

    @Test
    void testFirstTurn() {
        clickOn(messageField);
        assertEquals("Black's turn!!!", controller.textField.getText());
    }

    @Test
    void testSecondTurn() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(1));
        assertEquals("White's turn!!!", controller.textField.getText());
    }

    @Test
    void testBlackColorFilling() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(2));
        assertEquals(Color.BLACK, getColorOfCell(2));
    }

    @Test
    void testWhiteColorFilling() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(2));
        clickOn(controller.board.getChildren().get(4));
        assertEquals(Color.WHITE, getColorOfCell(4));
    }

    @Test
    void testResetButton() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(2));
        clickOn(resetButton);
        assertEquals("Click anywhere to start", messageField.getText());
    }

    @Test
    void testBackButton() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(0));
        clickOn(controller.board.getChildren().get(2));
        clickOn(goBackButton);
        Background backgroundAfterBack =
                ((Pane) controller.board.getChildren().get(2)).getBackground();
        assertTrue(backgroundAfterBack == null || backgroundAfterBack.getFills().isEmpty());
    }

    @Test
    void testBackButtonAfterNoMove() {
        clickOn(messageField);
        clickOn(goBackButton);
        assertEquals("No more memory left!", messageField.getText());
    }

    @Test
    void testBackButtonAfterOneMove() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(2));
        clickOn(goBackButton);
        assertEquals("No more memory left!", messageField.getText());
    }

    @Test
    void testForwardButton() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(0));
        clickOn(controller.board.getChildren().get(2)); // white 0xffffffff
        clickOn(goBackButton);
        clickOn(goForwardButton);
        assertEquals(Color.WHITE, getColorOfCell(2));
    }

    @Test
    void testInvalidForwardButtonAfterNoMove() {
        clickOn(messageField);
        clickOn(goForwardButton);
        assertEquals("Cannot go forward!", messageField.getText());
    }

    @Test
    void testInvalidForwardButtonAfterOneMove() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(2));
        clickOn(goForwardButton);
        assertEquals("Cannot go forward!", messageField.getText());
    }

    @Test
    void testExitButton() {
        AtomicBoolean exitCalled = new AtomicBoolean(false);
        controller.exitHandler = () -> exitCalled.set(true);
        clickOn("#exitButton");
        assertTrue(exitCalled.get());
    }

    @Test
    void testInvalidMove() {
        clickOn(messageField);
        clickOn(controller.board.getChildren().get(0));
        clickOn(controller.board.getChildren().get(4)); // casual, It's the white player
        clickOn(controller.board.getChildren().get(14));
        assertEquals("Invalid move!", messageField.getText());
    }

    @Test
    void testBlackWin() {
        clickOn(messageField);
        for (int row = 0; row < 13; row++) {
            // 2 and 4 are casual indexes
            int index1 = row * 13 + 2;
            int index2 = row * 13 + 4;
            clickOn(controller.board.getChildren().get(index1));
            if (row != 12) clickOn(controller.board.getChildren().get(index2));
        }
        assertEquals("BLACK WINS", messageField.getText());
    }

    @Test
    void testWhiteWin() {
        clickOn(messageField);
        for (int col = 0; col < 13; col++) {
            int index1 = 2 * 13 + col;
            int index2 = 4 * 13 + col;
            clickOn(controller.board.getChildren().get(index1));
            clickOn(controller.board.getChildren().get(index2));
        }
        assertEquals("WHITE WINS", messageField.getText());
    }

    @Test
    void testTerritoryFilling() {
        clickOn(messageField);
        for (int i = 14; i < 22; i++) clickOn(controller.board.getChildren().get(i));
        for (int i = 40; i < 48; i++) clickOn(controller.board.getChildren().get(i));
        clickOn(controller.board.getChildren().get(27));
        clickOn(controller.board.getChildren().get(34));
        for (int i = 28; i < 33; i++) assertEquals(Color.BLACK, getColorOfCell(i));
    }

    @Test
    void testOnlyTerritoryFilling() {
        clickOn(messageField);
        for (int i = 14; i < 22; i++) clickOn(controller.board.getChildren().get(i));
        for (int i = 40; i < 48; i++) clickOn(controller.board.getChildren().get(i));
        clickOn(controller.board.getChildren().get(27));
        clickOn(controller.board.getChildren().get(34));
        int[][] intervals = {
            {0, 14},
            {22, 27},
            {35, 40},
            {48, 169}
        };

        for (int[] interval : intervals) {
            for (int i = interval[0]; i < interval[1]; i++) {
                System.out.println(i);
                Background backgroundAfterBack =
                        ((Pane) controller.board.getChildren().get(i)).getBackground();
                assertTrue(backgroundAfterBack == null || backgroundAfterBack.getFills().isEmpty());
            }
        }
    }

    @Test
    void testBlackTerritoryFilling() {
        // we are creating a border with 5 black stone (indexes 14, 15...) and 5 white stones
        // (indexes 40, 41...),
        clickOn(messageField);
        for (int i = 14; i <= 18; i++) {
            clickOn(controller.board.getChildren().get(i));
            clickOn(
                    controller
                            .board
                            .getChildren()
                            .get(i + 86)); // causal offset, far from the other stone
        }
        for (int i = 40; i <= 44; i++) {
            clickOn(controller.board.getChildren().get(i));
            clickOn(controller.board.getChildren().get(i + 65));
        }
        // for closing the territory
        clickOn(controller.board.getChildren().get(27));
        clickOn(controller.board.getChildren().get(31));

        for (int i = 28; i < 31; i++) {
            assertEquals(Color.BLACK, getColorOfCell(i));
        }
    }

    @Test
    void testWhiteTerritoryFilling() {
        // we are creating a border with 4 black stone (indexes 14, 15...) s and 4 white stones
        // (indexes 40, 41...),
        clickOn(messageField);
        for (int i = 14; i <= 17; i++) clickOn(controller.board.getChildren().get(i));
        for (int i = 40; i <= 43; i++) clickOn(controller.board.getChildren().get(i));
        clickOn(controller.board.getChildren().get(100)); // causal index, far from the other stone
        // for closing the territory
        clickOn(controller.board.getChildren().get(26));
        clickOn(controller.board.getChildren().get(31));

        for (int i = 27; i < 30; i++) {
            assertEquals(Color.WHITE, getColorOfCell(i));
        }
    }

    Color getColorOfCell(int index) {
        Pane cell = (Pane) controller.board.getChildren().get(index);
        return (Color) cell.getBackground().getFills().getFirst().getFill();
    }
}
