module quentin {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports quentin;

    opens quentin to
            javafx.fxml;

    exports quentin.network;

    opens quentin.network to
            javafx.fxml;
}
