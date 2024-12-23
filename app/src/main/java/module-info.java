module quentin.game {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports quentin;

    opens quentin to
            javafx.fxml;
}
