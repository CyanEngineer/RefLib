module com.cyaneer.reflib {
    requires java.desktop;
    requires java.management;
    
    requires javafx.base;
    requires javafx.controls;
    requires transitive javafx.graphics;
    opens com.cyaneer.reflib to javafx.graphics;

    requires transitive org.bytedeco.opencv;

    requires com.fasterxml.jackson.databind;
    opens com.cyaneer.reflib.domain to com.fasterxml.jackson.databind;
    opens com.cyaneer.reflib.practice.domain to com.fasterxml.jackson.databind;
}
