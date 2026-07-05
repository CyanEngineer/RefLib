package com.cyaneer.reflib.refViewer;

import com.cyaneer.reflib.domain.Ref;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RefViewerModel {
    
    private final ObjectProperty<Ref> ref = new SimpleObjectProperty<Ref>(null);
    private final DoubleProperty imageOpacity = new SimpleDoubleProperty(1);
    private final DoubleProperty imageRotation = new SimpleDoubleProperty(0);

    public Ref getRef() {
        return ref.get();
    }

    public ObjectProperty<Ref> refProperty() {
        return ref;
    }

    public void setRef(Ref ref) {
        this.ref.set(ref);
    }

    public double getImageOpacity() {
        return imageOpacity.get();
    }

    public DoubleProperty imageOpacityProperty() {
        return imageOpacity;
    }

    public void setImageOpacity(double imageOpacity) {
        this.imageOpacity.set(imageOpacity);
    }

    public double getImageRotation() {
        return imageRotation.get();
    }

    public DoubleProperty imageRotationProperty() {
        return imageRotation;
    }

    public void setImageRotation(double imageRotation) {
        this.imageRotation.set(imageRotation);
    }
}
