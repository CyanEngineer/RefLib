package com.cyaneer.reflib.upload;

import java.net.URI;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.cyaneer.reflib.domain.MatchableRef;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class UploadController {
    
    private UploadModel model;
    private UploadInteractor interactor;
    private Builder<Region> viewBuilder;

    public UploadController(
        ListProperty<MatchableRef> masterRefList,
        ObjectProperty<Boolean> isRefListLoaded, //TODO: When false, diable upload functionality. Consider if it should be turned false between accept and the Ref being added to the list
        Runnable backAction,
        Function<URI, MatchableRef> createRefAction,
        BiConsumer<MatchableRef, Runnable> addRefAction
    ) {
        model = new UploadModel();

        interactor = new UploadInteractor(
            model,
            masterRefList,
            isRefListLoaded,
            createRefAction,
            addRefAction
        );

        viewBuilder = new UploadViewBuilder(
            model,
            backAction,
            uri -> proposeNewRef(uri),
            () -> acceptNewRef(),
            () -> rejectNewRef()
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void proposeNewRef(URI uri) {
        interactor.proposeNewRef(uri);
    }

    private void acceptNewRef() {
        interactor.addNewRef();
    }

    private void rejectNewRef() {
        interactor.clearNewRef();
    }
}
