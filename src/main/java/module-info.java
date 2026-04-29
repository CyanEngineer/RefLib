module com.cyaneer.reflib {
    requires java.management;
    
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.base;
    
    requires transitive org.bytedeco.opencv;

    exports com.cyaneer.reflib;
    exports com.cyaneer.reflib.model;
}
