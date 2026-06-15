package com.cyaneer.reflib.practice;

import com.cyaneer.reflib.domain.MatchableRef;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PracticeController {

    private PracticeModel model;
    private Builder<Region> viewBuilder;
    private PracticeInteractor interactor;

    public PracticeController(
        ListProperty<MatchableRef> refList,
        BooleanProperty isRefListLoaded
    ) {
        model = new PracticeModel();
        model.fullPoseListProperty().bind(refList);

        interactor = new PracticeInteractor(model, isRefListLoaded);
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

    private void loadSequence() { //TODO: Move to repository
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
