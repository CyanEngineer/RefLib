package com.cyaneer.reflib.upload;

import java.io.File;
import java.util.List;
import java.util.PriorityQueue;

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

import com.cyaneer.reflib.PracticeService;
import com.cyaneer.reflib.Ref;

import javafx.collections.FXCollections;

public class UploadInteractor {
    private UploadModel model;
    private PracticeService service = new PracticeService();
    private SIFT sift = SIFT.create();
    private BFMatcher matcher = new BFMatcher();
    private static final double MAX_DIM = 200;

    public UploadInteractor(UploadModel model) {
        this.model = model;
    }

    public void loadRefs() {
        List<Ref> refList = service.loadRefs();
        model.setRefList(FXCollections.observableArrayList(refList));
    }

    public void proposeNewRef(String filepath) {

        Ref newRef = service.createNewRef(filepath);

        Mat preppedImg = prepareImageForSIFT(newRef.getFile());
        Mat descriptors = computeDescriptors(preppedImg);
        newRef.setSIFTDescriptors(descriptors);

        model.setNewRef(newRef);

        // SIFT is not flip-robust, so a workaround is to use a double-image that contains
        // the image plus a flipped version of the image
        Ref newRefDouble = new Ref();
        newRefDouble.setSIFTDescriptors(computeDoubleSidedDescriptors(preppedImg));

        List<MatchedRef> mostSimilarRefs = findMostSimilarRefs(newRefDouble, model.getRefList(), model.getNumSimilarRefs());
        model.setMostSimilarRefs(FXCollections.observableArrayList(mostSimilarRefs));
    }

    public Mat prepareImageForSIFT(File ref) {
        Mat grayImg = imread(ref.getAbsolutePath(), IMREAD_GRAYSCALE);

        Mat preppedImg = new Mat();
        double scale = MAX_DIM / Math.max(grayImg.arrayHeight(), grayImg.arrayWidth());
        resize(grayImg, preppedImg, new Size(), scale, scale, INTER_LINEAR);

        return preppedImg;
    }

    public Mat computeDescriptors(Mat img) {
        Mat descriptors = new Mat();
        KeyPointVector keypoints = new KeyPointVector();
        sift.detectAndCompute(img, new Mat(), keypoints, descriptors);

        return descriptors;
    }

    public Mat computeDoubleSidedDescriptors(Mat image) {
        Mat preppedImgFlipped = new Mat();
        flip(image, preppedImgFlipped, 1);

        Mat preppedImgDouble = new Mat();
        hconcat(image, preppedImgFlipped, preppedImgDouble);

        Mat descriptorsDouble = computeDescriptors(preppedImgDouble);
        
        return descriptorsDouble;
    }

    public List<MatchedRef> findMostSimilarRefs(Ref ref, List<Ref> refList, int numSimilarRefs) {
        PriorityQueue<MatchedRef> similarRefs = new PriorityQueue<>();

        for (Ref candidate : refList) {
            similarRefs.add(matchReferences(ref, candidate));
        }

        List<MatchedRef> mostSimilarRefs = new java.util.ArrayList<>();
        for (int i = 0; i < numSimilarRefs; i++) {
            if (similarRefs.isEmpty()) break;

            MatchedRef matchedRef = similarRefs.poll();
            if (matchedRef.getNumMatches() == 0) break;

            mostSimilarRefs.add(matchedRef);
        }

        return mostSimilarRefs;
    }

    public MatchedRef matchReferences(Ref newRef, Ref candidate) {
        DMatchVectorVector matches = new DMatchVectorVector();
        matcher.knnMatch(newRef.getSIFTDescriptors(), candidate.getSIFTDescriptors(), matches, 2);
        
        int numGoodMatches = countGoodMatches(matches);
        return new MatchedRef(candidate, numGoodMatches);
    }

    public int countGoodMatches(DMatchVectorVector matches) {
        int numGoodMatches = 0;
        for (DMatchVector match : matches.get()) {
            if (match.get(0).distance() < 0.5 * match.get(1).distance()) {
                numGoodMatches++;
            }
        }
        return numGoodMatches;
    }

    //TODO: Integrate into service
    public void acceptNewRef() {
        model.getRefList().add(model.getNewRef());
        model.setNewRef(null);
        model.setMostSimilarRefs(FXCollections.observableArrayList());
    }

    public void rejectNewRef() {
        model.setNewRef(null);
        model.setMostSimilarRefs(FXCollections.observableArrayList());
    }
}