module com.rubiksproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.rubiksproject to javafx.fxml;
    exports com.rubiksproject;
}
