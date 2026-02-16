package com.cyaneer.reflib;

import com.cyaneer.reflib.model.PracticeModel;
import com.cyaneer.reflib.model.SequenceStepType;
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
        loadSequence();
        viewBuilder = new PracticeViewBuilder(
            model,
            type -> addStep(type),
            idx -> removeStep(idx),
            () -> startPractice(),
            () -> startPracticeTimer(),
            () -> pausePracticeTimer(),
            () -> stopPracticeTimer(),
            () -> jumpToNext(),
            () -> resetPractice()
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
        Thread loadImagesThread = new Thread(loadImagesTask);
        loadImagesThread.start();
    }

    private void loadSequence() {
        Task<Void> loadSequenceTask = new Task<Void>() {
            @Override
            protected Void call() {
                interactor.loadSequence();
                return null;
            }
        };
        Thread loadSequenceThread = new Thread(loadSequenceTask);
        loadSequenceThread.start();
    }

    private void addStep(SequenceStepType type) {
        interactor.addStep(type);
    }

    private void removeStep(int idx) {
        interactor.removeStep(idx);
    }

    private void startPractice() {
        interactor.startPractice();
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

    private void jumpToNext() {
        interactor.advanceInCurrentStep();
    }

    private void resetPractice() {
        interactor.resetPractice();
    }
}
