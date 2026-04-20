module com.cyaneer.reflib {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;

    opens com.cyaneer.reflib to javafx.fxml;
    exports com.cyaneer.reflib;
}
