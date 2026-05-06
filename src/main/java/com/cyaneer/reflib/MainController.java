package com.cyaneer.reflib;

import com.cyaneer.reflib.practice.PracticeController;
import com.cyaneer.reflib.upload.UploadController;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MainController {
    private Builder<Region> viewBuilder;
    private MainInteractor interactor;

    public MainController() {
        interactor = new MainInteractor();
        viewBuilder = new MainViewBuilder(
            new UploadController().getView(),
            new PracticeController().getView()
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
