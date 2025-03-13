module quentin.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens quentin.gui to
            javafx.fxml;

    exports quentin;
}
