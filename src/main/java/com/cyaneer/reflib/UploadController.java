package com.cyaneer.reflib;

import com.cyaneer.reflib.model.UploadModel;
import com.cyaneer.reflib.viewBuilder.UploadViewBuilder;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class UploadController {
    
    private UploadModel model;
    private Builder<Region> viewBuilder;
    private UploadInteractor interactor;

    public UploadController() {
        model = new UploadModel();
        interactor = new UploadInteractor(model);
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

    private void loadRefs() {
        Task<Void> loadRefsTask = new Task<Void>() {
            @Override
            protected Void call() {
                interactor.loadRefs();
                return null;
            }
        };
        Thread loadRefsThread = new Thread(loadRefsTask);
        loadRefsThread.start();
    }

    private void proposeNewRef(String path) {
        interactor.proposeNewRef(path);
    }

    private void acceptNewRef() {
        interactor.acceptNewRef();
    }

    private void rejectNewRef() {
        interactor.rejectNewRef();
    }
}
