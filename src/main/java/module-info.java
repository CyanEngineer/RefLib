module com.cyaneer.reflib {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.cyaneer.reflib to javafx.fxml;
    exports com.cyaneer.reflib;
}
