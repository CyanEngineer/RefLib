package com.cyaneer.reflib.upload;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.cyaneer.reflib.MatchableRef;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class UploadController {
    
    private UploadModel model;
    private UploadInteractor interactor;
    private Builder<Region> viewBuilder;
    private BiConsumer<MatchableRef, Runnable> addRefAction;

    public UploadController(
        ListProperty<MatchableRef> refList,
        BooleanProperty isRefListLoaded, //TODO: When false, diable upload functionality. Consider if it should be turned false between accept and the Ref being added to the list
        Function<String, MatchableRef> createRefAction,
        BiConsumer<MatchableRef, Runnable> addRefAction
    ) {
        model = new UploadModel();
        model.refListProperty().bind(refList);
        model.isRefListLoadedProperty().bind(isRefListLoaded);

        this.addRefAction = addRefAction;

        interactor = new UploadInteractor(
            model,
            createRefAction
        );

        viewBuilder = new UploadViewBuilder(
            model,
            path -> proposeNewRef(path),
            () -> acceptNewRef(),
            () -> rejectNewRef()
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void proposeNewRef(String path) {
        interactor.proposeNewRef(path);
    }

    private void acceptNewRef() {
        addRefAction.accept(
            model.getNewRef(),
            () -> interactor.clearNewRef()
        );
    }

    private void rejectNewRef() {
        interactor.clearNewRef();
    }
}
