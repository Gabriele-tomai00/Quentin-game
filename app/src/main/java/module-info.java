module quentin.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports quentin.game;
    exports quentin.cache;
    exports quentin.gui;
    exports quentin;

    opens quentin.gui to
            javafx.fxml;
}
