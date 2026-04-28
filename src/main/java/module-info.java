module com.cyaneer.reflib {
    requires java.management;
    
    requires javafx.base;
    requires javafx.controls;
    requires transitive javafx.graphics;

    exports com.cyaneer.reflib;
    exports com.cyaneer.reflib.model;
}
