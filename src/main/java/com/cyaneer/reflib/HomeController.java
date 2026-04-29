package com.cyaneer.reflib;

import com.cyaneer.reflib.viewBuilder.HomeViewBuilder;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HomeController {
    private Builder<Region> viewBuilder;
    private HomeInteractor interactor;

    public HomeController() {
        interactor = new HomeInteractor();
        viewBuilder = new HomeViewBuilder(
            new UploadController().getView(),
            new PracticeController().getView()
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
