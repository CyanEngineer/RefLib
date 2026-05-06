package com.cyaneer.reflib.upload;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bytedeco.opencv.opencv_core.Mat;
import org.junit.jupiter.api.Test;

import static org.bytedeco.opencv.global.opencv_core.rotate;
import static org.bytedeco.opencv.global.opencv_core.ROTATE_90_CLOCKWISE;

import com.cyaneer.reflib.Ref;

public class UploadInteractorTest {
    
    @Test
    public void testPrepareImageForSIFTReturnsImageWithMaxDim200() {
        UploadInteractor interactor = new UploadInteractor(null);
        Mat preppedImg = interactor.prepareImageForSIFT(new File("src/test/resources/testimage.png"));
        assert(preppedImg.size().width() <= 200);
        assert(preppedImg.size().height() <= 200);
    }
    
    @Test
    public void testPrepareImageForSIFTReturnsGreyscaleImage() {
        UploadInteractor interactor = new UploadInteractor(null);
        Mat preppedImg = interactor.prepareImageForSIFT(new File("src/test/resources/testimage.png"));
        assert(preppedImg.channels() == 1);
    }

    @Test
    public void testMatchReferencesFindsNoMatchesWhenDescriptorsAreMissing() {
        UploadInteractor interactor = new UploadInteractor(null);
        
        Ref ref1 = new Ref(new File("src/test/resources/testimage.png"));
        Ref ref2 = new Ref(new File("src/test/resources/testimageedited.png"));
        MatchedRef matchedRef = interactor.matchReferences(ref1, ref2);
        assert(matchedRef.getNumMatches() == 0);

        ref1.setSIFTDescriptors(new Mat());
        ref2.setSIFTDescriptors(new Mat());
        matchedRef = interactor.matchReferences(ref1, ref2);
        assert(matchedRef.getNumMatches() == 0);
    }

    @Test
    public void testMatchReferencesMatchesAllDescriptorsWhenMatchingSelf() {
        UploadInteractor interactor = new UploadInteractor(null);
        
        Ref ref = new Ref(new File("src/test/resources/testimage.png"));
        Mat preppedImg = interactor.prepareImageForSIFT(ref.getFile());
        Mat descriptors = interactor.computeDescriptors(preppedImg);
        ref.setSIFTDescriptors(descriptors);

        MatchedRef matchedRef = interactor.matchReferences(ref, ref);
        assert(matchedRef.getNumMatches() == descriptors.rows());
    }

    @Test
    public void testMatchReferencesMatchesMostDescriptorsWhenMatchingSelfRotated() {
        double threshold = 0.80;
        UploadInteractor interactor = new UploadInteractor(null);
        
        Ref ref1 = new Ref(new File("src/test/resources/testimage.png"));
        Mat preppedImg1 = interactor.prepareImageForSIFT(ref1.getFile());
        
        Ref ref2 = new Ref();
        Mat preppedImgFlipped = new Mat();
        rotate(preppedImg1, preppedImgFlipped, ROTATE_90_CLOCKWISE);
        
        ref1.setSIFTDescriptors(interactor.computeDescriptors(preppedImg1));
        ref2.setSIFTDescriptors(interactor.computeDescriptors(preppedImgFlipped));

        MatchedRef matchedRef = interactor.matchReferences(ref1, ref2);
        assert(matchedRef.getNumMatches() > ref1.getSIFTDescriptors().rows()*threshold);
    }

    @Test
    public void testMatchReferenceMatchesManyDescriptorsWhenMatchingEditedImage() {
        double threshold = 0.25;
        UploadInteractor interactor = new UploadInteractor(null);
        
        Ref ref1 = new Ref(new File("src/test/resources/testimage.png"));
        Mat preppedImg1 = interactor.prepareImageForSIFT(ref1.getFile());
        
        Ref ref2 = new Ref(new File("src/test/resources/testimageedited.png"));
        Mat preppedImg2 = interactor.prepareImageForSIFT(ref2.getFile());
        
        ref1.setSIFTDescriptors(interactor.computeDescriptors(preppedImg1));
        ref2.setSIFTDescriptors(interactor.computeDescriptors(preppedImg2));

        MatchedRef matchedRef = interactor.matchReferences(ref1, ref2);
        assert(matchedRef.getNumMatches() > ref1.getSIFTDescriptors().rows()*threshold);
    }

    @Test
    public void testMatchReferenceMatchesFewDescriptorsWhenMatchingDifferentImage() {
        double threshold = 0.05;
        UploadInteractor interactor = new UploadInteractor(null);
        
        Ref ref1 = new Ref(new File("src/test/resources/testimage.png"));
        Mat preppedImg1 = interactor.prepareImageForSIFT(ref1.getFile());
        
        Ref ref2 = new Ref(new File("src/test/resources/testimagedifferent.jpg"));
        Mat preppedImg2 = interactor.prepareImageForSIFT(ref2.getFile());
        
        ref1.setSIFTDescriptors(interactor.computeDescriptors(preppedImg1));
        ref2.setSIFTDescriptors(interactor.computeDescriptors(preppedImg2));

        MatchedRef matchedRef = interactor.matchReferences(ref1, ref2);
        assert(matchedRef.getNumMatches() < ref1.getSIFTDescriptors().rows()*threshold);
    }

    @Test
    public void testFindMostSimilarRefsReturnsThatSelfIsMostSimilar() {
        UploadInteractor interactor = new UploadInteractor(null);

        Ref ref1 = new Ref(new File("src/test/resources/testimage.png"));
        ref1.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref1.getFile())));

        Ref ref2 = new Ref(new File("src/test/resources/testimageedited.png"));
        ref2.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref2.getFile())));

        Ref ref3 = new Ref(new File("src/test/resources/testimagedifferent.jpg"));
        ref3.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref3.getFile())));

        List<Ref> refList = Arrays.asList(ref1, ref2, ref3);
        List<MatchedRef> matchedRefs1 = interactor.findMostSimilarRefs(ref1, refList, 3);
        assert(matchedRefs1.get(0).getRef() == ref1);

        List<Ref> refListReverse = Arrays.asList(ref3, ref2, ref1);
        List<MatchedRef> matchedRefs2 = interactor.findMostSimilarRefs(ref1, refListReverse, 3);
        assert(matchedRefs2.get(0).getRef() == ref1);
    }

    @Test
    public void testFindMostSimilarRefsReturnsThatEditedRefIsSecondMostSimilar() {
        UploadInteractor interactor = new UploadInteractor(null);

        Ref ref1 = new Ref(new File("src/test/resources/testimage.png"));
        ref1.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref1.getFile())));

        Ref ref2 = new Ref(new File("src/test/resources/testimageedited.png"));
        ref2.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref2.getFile())));

        Ref ref3 = new Ref(new File("src/test/resources/testimagedifferent.jpg"));
        ref3.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref3.getFile())));

        List<Ref> refList = Arrays.asList(ref1, ref2, ref3);
        List<MatchedRef> matchedRefs1 = interactor.findMostSimilarRefs(ref1, refList, 3);
        assert(matchedRefs1.get(1).getRef() == ref2);

        List<Ref> refListReverse = Arrays.asList(ref3, ref2, ref1);
        List<MatchedRef> matchedRefs2 = interactor.findMostSimilarRefs(ref1, refListReverse, 3);
        assert(matchedRefs2.get(1).getRef() == ref2);
    }

    @Test
    public void testProposeNewRefSetsNewRefFieldInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        assert(model.getNewRef() == null);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getNewRef() != null);
        assert(model.getNewRef().getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png"));
    }

    @Test
    public void testProposeNewRefSetsNoSimilarRefsWhenFirstRefIsProposed() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        assert(model.getMostSimilarRefs().isEmpty());

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getMostSimilarRefs().isEmpty());
    }

    @Test
    public void testProposeNewRefSetsSimilarRefsWhenEditedRefIsProposed() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        Ref ref1 = new Ref(new File("src/test/resources/testimage.png"));
        ref1.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref1.getFile())));
        model.getRefList().add(ref1);

        assert(model.getMostSimilarRefs().isEmpty());

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());
    }

    @Test
    public void testAcceptNewRefAddsRefToEmptyRefListInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getRefList().isEmpty());

        interactor.acceptNewRef();

        assert(!model.getRefList().isEmpty());
        assert(model.getRefList().get(0).getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png"));
    }

    @Test
    public void testAcceptNewRefAddsRefToNonemptyRefListInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        Ref ref = new Ref(new File("src/test/resources/testimage.png"));
        ref.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref.getFile())));
        model.getRefList().add(ref);

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimagedifferent.jpg");
        interactor.acceptNewRef();

        assert(model.getRefList().size() == 2);

        assert(model.getRefList().stream().anyMatch(x -> x.getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png")));
    }

    @Test
    public void testAcceptNewRefClearsNewRef() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getNewRef() != null);

        interactor.acceptNewRef();

        assert(model.getNewRef() == null);
    }

    @Test
    public void testAcceptNewRefClearsMostSimilarRefs() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        Ref ref = new Ref(new File("src/test/resources/testimage.png"));
        ref.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref.getFile())));
        model.getRefList().add(ref);

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());

        interactor.acceptNewRef();

        assert(model.getMostSimilarRefs().isEmpty());
    }

    @Test
    public void testRejectNewRefDoesNotAddRefToRefListInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getRefList().isEmpty());

        interactor.rejectNewRef();

        assert(model.getRefList().isEmpty());
    }

    @Test
    public void testRejectNewRefClearsNewRef() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getNewRef() != null);

        interactor.rejectNewRef();

        assert(model.getNewRef() == null);
    }

    @Test
    public void testRejectNewRefClearsMostSimilarRefs() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        Ref ref = new Ref(new File("src/test/resources/testimage.png"));
        ref.setSIFTDescriptors(interactor.computeDescriptors(interactor.prepareImageForSIFT(ref.getFile())));
        model.getRefList().add(ref);

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());

        interactor.rejectNewRef();

        assert(model.getMostSimilarRefs().isEmpty());
    }
}
