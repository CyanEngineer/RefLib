package com.cyaneer.reflib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.DMatchVector;
import org.bytedeco.opencv.opencv_core.DMatchVectorVector;
import org.bytedeco.opencv.opencv_core.KeyPointVector;

import org.bytedeco.opencv.opencv_features2d.SIFT;
import org.bytedeco.opencv.opencv_features2d.BFMatcher;

import static org.bytedeco.opencv.global.opencv_core.flip;
import static org.bytedeco.opencv.global.opencv_core.hconcat;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import static org.bytedeco.opencv.global.opencv_imgproc.INTER_LINEAR;

public class SIFTMatchableRef extends MatchableRef {
    
    private static final double PREPPED_IMG_MAX_DIM = 200;
    private static final double MATCH_THRESHOLD = 0.5;
    private static final SIFT sift = SIFT.create();
    private static final BFMatcher matcher = new BFMatcher();

    private File file;
    private Mat SIFTDescriptors;

    public SIFTMatchableRef(File file) {
        this.file = file;
        SIFTDescriptors = computeDescriptors(prepareImageForSIFT(file));
    }

    public SIFTMatchableRef(File file, Mat SIFTDescriptors) {
        this.file = file;
        this.SIFTDescriptors = SIFTDescriptors;
    }

    public File getFile() {
        return file;
    }

    public Mat getSIFTDescriptors() {
        return SIFTDescriptors;
    }

    @Override
    public Integer computeMatch(MatchableRef other) {
        if (!(other instanceof SIFTMatchableRef)) {
            throw new IllegalArgumentException("Cannot compare SIFT with non-SIFT");
        }
        SIFTMatchableRef otherSIFT = (SIFTMatchableRef) other;
        
        Mat doubleSidedSIFTDescriptors = computeDescriptors(createDoubleSidedImage(prepareImageForSIFT(file)));
        
        return computeGoodMatches(doubleSidedSIFTDescriptors, otherSIFT.getSIFTDescriptors());
    }

    @Override
    public List<Integer> computeAllMatches(List<MatchableRef> others) {
        List<SIFTMatchableRef> othersSIFT = new ArrayList<>(others.size());
        for (MatchableRef ref : others) {
            if (!(ref instanceof SIFTMatchableRef)) {
                throw new IllegalArgumentException("Cannot compare SIFT with non-SIFT");
            }
            othersSIFT.add((SIFTMatchableRef) ref);
        }

        Mat doubleSidedSIFTDescriptors = computeDescriptors(createDoubleSidedImage(prepareImageForSIFT(file)));

        List<Integer> goodMatches = new ArrayList<>(others.size());
        for (SIFTMatchableRef otherSIFT : othersSIFT) {
            goodMatches.add(computeGoodMatches(doubleSidedSIFTDescriptors, otherSIFT.getSIFTDescriptors()));
        }
        
        return goodMatches;
    }

    private int computeGoodMatches(Mat thisSIFTDescriptors, Mat otherSIFTDescriptors) {
        DMatchVectorVector matches = new DMatchVectorVector();
        matcher.knnMatch(thisSIFTDescriptors, otherSIFTDescriptors, matches, 2);
        
        return filterGoodMatches(matches);
    }

    private int filterGoodMatches(DMatchVectorVector matches) {
        int numGoodMatches = 0;
        for (DMatchVector match : matches.get()) {
            if (match.get(0).distance() < MATCH_THRESHOLD * match.get(1).distance()) {
                numGoodMatches++;
            }
        }
        return numGoodMatches;
    }

    private Mat prepareImageForSIFT(File ref) {
        Mat grayImg = imread(ref.getAbsolutePath(), IMREAD_GRAYSCALE);

        Mat preppedImg = new Mat();
        double scale = PREPPED_IMG_MAX_DIM / Math.max(grayImg.arrayHeight(), grayImg.arrayWidth());
        resize(grayImg, preppedImg, new Size(), scale, scale, INTER_LINEAR);

        return preppedImg;
    }

    // SIFT is not flip-robust, so a workaround is to use a double-image that contains
    // the image plus a flipped version of the image
    private Mat createDoubleSidedImage(Mat image) {
        Mat preppedImgFlipped = new Mat();
        flip(image, preppedImgFlipped, 1);

        Mat preppedImgDouble = new Mat();
        hconcat(image, preppedImgFlipped, preppedImgDouble);

        return preppedImgDouble;
    }

    private Mat computeDescriptors(Mat img) {
        Mat descriptors = new Mat();
        KeyPointVector keypoints = new KeyPointVector();
        sift.detectAndCompute(img, new Mat(), keypoints, descriptors);

        return descriptors;
    }
}
