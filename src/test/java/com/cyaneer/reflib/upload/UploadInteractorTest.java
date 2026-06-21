package com.cyaneer.reflib.upload;

import org.junit.jupiter.api.Test;

import java.io.File;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.domain.SIFTMatchableRef;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class UploadInteractorTest {

    @Test
    public void testProposeNewRefSetsNewRefFieldInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        assert(model.getNewRef() == null);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getNewRef() != null);
        assert(model.getNewRef().getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png"));
    }

    @Test
    public void testProposeNewRefSetsNoSimilarRefsWhenFirstRefIsProposed() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        assert(model.getMostSimilarRefs().isEmpty());

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getMostSimilarRefs().isEmpty());
    }

    @Test
    public void testAcceptNewRefAddsRefToEmptyRefListInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getRefList().isEmpty());

        interactor.addNewRef();

        assert(!model.getRefList().isEmpty());
        assert(model.getRefList().get(0).getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png"));
    }

    @Test
    public void testAcceptNewRefAddsRefToNonemptyRefListInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        interactor.proposeNewRef("src/test/resources/testimage.png");
        interactor.addNewRef();

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimagedifferent.jpg");
        interactor.addNewRef();

        assert(model.getRefList().size() == 2);

        assert(model.getRefList().stream().anyMatch(x -> x.getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png")));
    }

    @Test
    public void testAcceptNewRefClearsNewRef() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getNewRef() != null);

        interactor.addNewRef();

        assert(model.getNewRef() == null);
    }

    @Test
    public void testProposeNewRefSetsSimilarRefsWhenEditedRefIsProposed() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        interactor.proposeNewRef("src/test/resources/testimage.png");
        interactor.addNewRef();

        assert(model.getMostSimilarRefs().isEmpty());

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());
    }

    @Test
    public void testAcceptNewRefClearsMostSimilarRefs() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        interactor.proposeNewRef("src/test/resources/testimage.png");
        interactor.addNewRef();

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());

        interactor.addNewRef();

        assert(model.getMostSimilarRefs().isEmpty());
    }

    @Test
    public void testClearNewRefDoesNotAddRefToRefListInModel() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getRefList().isEmpty());

        interactor.clearNewRef();

        assert(model.getRefList().isEmpty());
    }

    @Test
    public void testClearNewRefClearsNewRef() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        interactor.proposeNewRef("src/test/resources/testimage.png");

        assert(model.getNewRef() != null);

        interactor.clearNewRef();

        assert(model.getNewRef() == null);
    }

    @Test
    public void testClearNewRefClearsMostSimilarRefs() {
        UploadModel model = new UploadModel();
        UploadInteractor interactor = createUploadInteractor(model, false);

        interactor.proposeNewRef("src/test/resources/testimage.png");
        interactor.addNewRef();

        assert(model.getRefList().size() == 1);

        interactor.proposeNewRef("src/test/resources/testimageedited.png");

        assert(!model.getMostSimilarRefs().isEmpty());

        interactor.clearNewRef();

        assert(model.getMostSimilarRefs().isEmpty());
    }

    private UploadInteractor createUploadInteractor(UploadModel model, boolean isRefListLoaded) {
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());

        return new UploadInteractor(
            model,
            masterRefList,
            new SimpleObjectProperty<Boolean>(isRefListLoaded),
            path -> new SIFTMatchableRef(new File(path)),
            (ref, cleanupAction) -> {masterRefList.add(ref); cleanupAction.run();}
        );
    }
}
