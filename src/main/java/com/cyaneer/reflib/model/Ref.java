package com.cyaneer.reflib.model;

import java.io.File;

import org.bytedeco.opencv.opencv_core.Mat;

public class Ref {
    
    private File file;
    private Mat SIFTDescriptors = new Mat();

    public Ref() {

    }

    public Ref(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Mat getSIFTDescriptors() {
        return SIFTDescriptors;
    }

    public void setSIFTDescriptors(Mat SIFTDescriptors) {
        this.SIFTDescriptors = SIFTDescriptors;
    }
}

