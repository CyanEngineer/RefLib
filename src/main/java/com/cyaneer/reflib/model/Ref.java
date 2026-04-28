package com.cyaneer.reflib.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;

import org.bytedeco.opencv.opencv_core.Mat;

public class Ref {
    
    private ObjectProperty<File> file = new SimpleObjectProperty<File>(null);
    private ObjectProperty<Mat> SIFTDescriptors = new SimpleObjectProperty<Mat>(null);

    public Ref() {

    }

    public Ref(File file) {
        this.file.set(file);
    }

    public File getFile() {
        return file.get();
    }

    public void setFile(File file) {
        this.file.set(file);
    }

    public ObjectProperty<File> fileProperty() {
        return file;
    }

    public Mat getSIFTDescriptors() {
        return SIFTDescriptors.get();
    }

    public void setSIFTDescriptors(Mat SIFTDescriptors) {
        this.SIFTDescriptors.set(SIFTDescriptors);
    }

    public ObjectProperty<Mat> SIFTDescriptorsProperty() {
        return SIFTDescriptors;
    }
}

