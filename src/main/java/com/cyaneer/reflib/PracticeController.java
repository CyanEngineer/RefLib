package com.cyaneer.reflib;

import com.cyaneer.reflib.viewBuilder.PracticeViewBuilder;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PracticeController {

    private PracticeModel model;
    private Builder<Region> viewBuilder;
    private PracticeInteractor interactor;

    public PracticeController() {
        model = new PracticeModel();
        interactor = new PracticeInteractor(model);
        loadImages();
        viewBuilder = new PracticeViewBuilder(
            model,
            () -> startPracticeTimer(),
            () -> pausePracticeTimer(),
            () -> stopPracticeTimer()
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void loadImages() {
        Task<Void> loadImagesTask = new Task<Void>() {
            @Override
            protected Void call() {
                interactor.loadImages();
                return null;
            }
        };
        loadImagesTask.setOnSucceeded(e -> {
            System.out.println(model.getPoseList().size() + " poses loaded"); //TODO: delete
        });
        Thread loadImagesThread = new Thread(loadImagesTask);
        loadImagesThread.start();
    }

    private void startPracticeTimer() {
        interactor.startTimer();
    }

    private void pausePracticeTimer() {
        interactor.pauseTimer();
    }

    private void stopPracticeTimer() {
        interactor.stopTimer();
    }
}
