module quentin.game {
    requires javafx.controls;
    requires javafx.fxml;

    exports quentin;

    opens quentin to
            javafx.fxml;
}
