module ClientApp {
    requires javafx.controls;
    requires javafx.fxml;


    exports Client;
    opens Client to javafx.fxml;
}