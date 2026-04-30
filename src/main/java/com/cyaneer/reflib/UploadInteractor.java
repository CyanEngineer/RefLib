package com.cyaneer.reflib;

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

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import static org.bytedeco.opencv.global.opencv_imgproc.INTER_LINEAR;

import com.cyaneer.reflib.model.MatchedRef;
import com.cyaneer.reflib.model.Ref;
import com.cyaneer.reflib.model.UploadModel;

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

        // Figure out how to handle the flipped version. Is it possible to flip the descriptors instead of the image?
        Mat descriptors = computeDescriptors(newRef.getFile());
        newRef.setSIFTDescriptors(descriptors);

        model.setNewRef(newRef);

        List<MatchedRef> mostSimilarRefs = findMostSimilarRefs(newRef);
        model.setMostSimilarRefs(FXCollections.observableArrayList(mostSimilarRefs));
    }

    private Mat computeDescriptors(File ref) {
        Mat preppedImg = prepareImage(ref);
        
        Mat descriptors = new Mat();
        KeyPointVector keypoints = new KeyPointVector();
        sift.detectAndCompute(preppedImg, new Mat(), keypoints, descriptors);

        return descriptors;
    }

    private Mat prepareImage(File ref) {
        Mat grayImg = imread(ref.getAbsolutePath(), IMREAD_GRAYSCALE);

        Mat preppedImg = new Mat();
        double scale = MAX_DIM / Math.max(grayImg.arrayHeight(), grayImg.arrayWidth());
        resize(grayImg, preppedImg, new Size(), scale, scale, INTER_LINEAR);

        return preppedImg;
    }

    private List<MatchedRef> findMostSimilarRefs(Ref ref) {
        PriorityQueue<MatchedRef> similarRefs = new PriorityQueue<>();

        for (Ref candidate : model.getRefList()) {
            DMatchVectorVector matches = new DMatchVectorVector();
            matcher.knnMatch(ref.getSIFTDescriptors(), candidate.getSIFTDescriptors(), matches, 2);

            int numGoodMatches = countGoodMatches(matches);

            similarRefs.add(new MatchedRef(candidate, numGoodMatches));
        }

        List<MatchedRef> mostSimilarRefs = new java.util.ArrayList<>();
        for (int i = 0; i < model.getNumSimilarRefs(); i++) {
            if (similarRefs.isEmpty()) break;

            MatchedRef matchedRef = similarRefs.poll();
            if (matchedRef.getNumMatches() == 0) break;

            mostSimilarRefs.add(matchedRef);
        }

        return mostSimilarRefs;
    }

    private int countGoodMatches(DMatchVectorVector matches) {
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