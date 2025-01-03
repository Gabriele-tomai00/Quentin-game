module quentin {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports quentin to
            javafx.graphics;
    exports quentin.game;
    exports quentin.cache;

    opens quentin to
            javafx.fxml;
}
