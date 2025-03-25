module quentin.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports quentin;

    opens quentin.network to
            javafx.fxml;
    opens quentin.gui to
            javafx.fxml;

    exports quentin.network;
    exports quentin.game;
    exports quentin.cache;
    exports quentin.gui;
}
