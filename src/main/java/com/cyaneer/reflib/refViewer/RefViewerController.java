package com.cyaneer.reflib.refViewer;

import com.cyaneer.reflib.domain.Ref;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class RefViewerController {
    private RefViewerModel model;
    private Builder<Region> viewBuilder;
    private RefViewerInteractor interactor;
    
    public RefViewerController(Ref ref) {
        model = new RefViewerModel();
        model.setRef(ref);

        interactor = new RefViewerInteractor(model);

        viewBuilder = new RefViewerViewBuilder(model);
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
