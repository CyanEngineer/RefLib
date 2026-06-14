package com.cyaneer.reflib.upload;

import org.junit.jupiter.api.Test;

public class UploadInteractorTest {

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
    public void testAcceptNewRefAddsRefToEmptyRefListInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getRefList().isEmpty());

        interactor.clearNewRef();

        assert(!model.getRefList().isEmpty());
        assert(model.getRefList().get(0).getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png"));
    }

    @Test
    public void testAcceptNewRefAddsRefToNonemptyRefListInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");
        interactor.clearNewRef();

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimagedifferent.jpg");
        interactor.clearNewRef();

        assert(model.getRefList().size() == 2);

        assert(model.getRefList().stream().anyMatch(x -> x.getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png")));
    }

    @Test
    public void testAcceptNewRefClearsNewRef() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getNewRef() != null);

        interactor.clearNewRef();

        assert(model.getNewRef() == null);
    }

    @Test
    public void testProposeNewRefSetsSimilarRefsWhenEditedRefIsProposed() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");
        interactor.clearNewRef();

        assert(model.getMostSimilarRefs().isEmpty());

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());
    }

    @Test
    public void testAcceptNewRefClearsMostSimilarRefs() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = new UploadInteractor(model);

        interactor.proposeNewRef("src/test/resources/testimage.png");
        interactor.clearNewRef();

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());

        interactor.clearNewRef();

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

        interactor.proposeNewRef("src/test/resources/testimage.png");
        interactor.clearNewRef();

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());

        interactor.rejectNewRef();

        assert(model.getMostSimilarRefs().isEmpty());
    }
}
