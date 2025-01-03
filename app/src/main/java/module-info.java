module quentin {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports quentin to
            javafx.graphics;
    exports quentin.game;

    opens quentin to
            javafx.fxml;
}
