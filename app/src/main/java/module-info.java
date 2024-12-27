module quentin.game {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports quentin;
    exports quentin.cache;
    exports quentin.game;

    opens quentin to
            javafx.fxml;
}
