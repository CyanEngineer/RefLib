module com.cyaneer.reflib {
    requires java.management;
    
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.base;
    
    requires transitive org.bytedeco.opencv;

    requires com.fasterxml.jackson.databind;
    opens com.cyaneer.reflib.domain to com.fasterxml.jackson.databind;
}
