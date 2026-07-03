package com.cyaneer.reflib.practice;

import java.io.IOException;
import java.util.function.Consumer;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.practice.domain.Sequence;
import com.cyaneer.reflib.practice.domain.SequenceStepType;
import com.cyaneer.reflib.practice.repository.JSONSequenceRepository;
import com.cyaneer.reflib.practice.repository.SequenceRepository;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PracticeController {

    private PracticeModel model;
    private Builder<Region> viewBuilder;
    private PracticeInteractor interactor;

    public PracticeController(
        ListProperty<MatchableRef> masterRefList,
        ObjectProperty<Boolean> isRefListLoaded
    ) {
        model = new PracticeModel();

        SequenceRepository repository = null;
        try { // TODO: Handle
            repository = new JSONSequenceRepository();
        } catch (IOException e) {
            e.printStackTrace();
        }

        interactor = new PracticeInteractor(
            model,
            repository,
            masterRefList,
            isRefListLoaded
        );

        loadSequences();

        viewBuilder = new PracticeViewBuilder(
            model,
            type -> addStep(type),
            idx -> removeStep(idx),
            sequence -> setCurrentSequence(sequence),
            idx -> saveCurrentSequence(idx),
            (idx, callback) -> deleteCurrentSequence(idx, callback),
            (name, callback) -> createNewSequence(name, callback),
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

    private void loadSequences() {
        Task<Void> loadSequenceTask = new Task<Void>() {
            @Override
            protected Void call() {
                try { // TODO: Handle
                    interactor.loadSequences();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        Thread loadSequenceThread = new Thread(loadSequenceTask);
        loadSequenceThread.start();
    }

    private void setCurrentSequence(Sequence sequence) {
        interactor.setCurrentSequence(sequence);
    }

    private void saveCurrentSequence(int i) {
        interactor.saveCurrentSequence(i);
        saveSequences();
    }

    private void deleteCurrentSequence(int i, Consumer<Sequence> callback) {
        interactor.deleteCurrentSequence(i, callback);
        saveSequences();
    }

    private void createNewSequence(String name, Consumer<Sequence> callback) {
        interactor.createNewSequence(name, callback);
    }

    private void saveSequences() {
        Task<Void> saveSequenceTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    interactor.saveSequences();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        Thread saveSequenceThread = new Thread(saveSequenceTask);
        saveSequenceThread.start();
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
