package com.cyaneer.reflib.practice;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.practice.domain.Sequence;
import com.cyaneer.reflib.practice.domain.SequenceStep;
import com.cyaneer.reflib.practice.domain.SequenceStepType;
import com.cyaneer.reflib.practice.repository.SequenceRepository;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.util.Duration;

public class PracticeInteractor {
    private PracticeModel model;
    private SequenceRepository repository;
    private Timeline timer = new Timeline();

    public PracticeInteractor (
        PracticeModel model,
        SequenceRepository repository,
        ListProperty<MatchableRef> masterRefList,
        ObjectProperty<Boolean> isRefListLoaded
    ) {
        this.model = model;
        model.fullRefListProperty().bind(masterRefList);

        this.repository = repository;

        isRefListLoaded.addListener((obs, oldValue, newValue) -> {
            if (newValue) resetPractice();
        });
        if (isRefListLoaded.get()) {
            resetPractice();
        }

        model.timerStatusProperty().bind(timer.statusProperty());
        
        // Only update elapsedSeconds every second
        timer.currentTimeProperty().addListener((obs, oldValue, newValue) -> {
            int oldSec = (int) oldValue.toSeconds();
            int newSec = (int) newValue.toSeconds();
            if (newSec != oldSec) {
                model.setElapsedSeconds(newSec);
            }
        });
    }

    public void loadSequences() throws IOException {
        List<Sequence> sequences = repository.loadSequences();
        model.setSequenceList(sequences);
        model.setCurrentSequence(sequences.get(0));
    }

    public void saveCurrentSequence(int i) {
        if (i >= 0 && i < model.getSequenceList().size()) {
            Sequence modelSequence = model.getSequenceList().get(i);
            modelSequence.setName(model.getCurrentSequence().getName());
            modelSequence.setSteps(model.getCurrentSequence().deepCopySteps());
        } else if (i == model.getSequenceList().size()) {
            model.getSequenceList().add(model.getCurrentSequence());
        } else { //TODO: Test
            throw new IndexOutOfBoundsException("Sequence index i=" + i + 
                "is out of bounds i=[0," + (model.getSequenceList().size()-1) +
                "] or i=" + model.getSequenceList().size() + " for adding a new sequence");
        }
    }

    public void deleteCurrentSequence(int i, Consumer<Sequence> callback) {
        if (i >= 0 && i < model.getSequenceList().size()) {
            model.getSequenceList().remove(i);

            if (model.getSequenceList().size() > 0) {
                callback.accept(model.getSequenceList().get(0));
            } else {
                callback.accept(new Sequence());
            }
        } else { //TODO: Test
            throw new IndexOutOfBoundsException("Sequence index i=" + i + 
                "is out of bounds i=[0," + (model.getSequenceList().size()-1) + "]");
        }
    }

    public void saveSequences() throws IOException {
        repository.saveSequences(model.getSequenceList());
    }

    public void createNewSequence(String name, Consumer<Sequence> callback) {
        Sequence sequence = new Sequence();
        sequence.setName(name);
        model.getSequenceList().add(sequence);
        callback.accept(sequence);
    }

    public void setCurrentSequence(Sequence sequence) {
        model.setCurrentSequence(sequence.createDeepCopy());
    }

    public void addStep(SequenceStepType type) {
        int repetitions = type == SequenceStepType.BREAK ? 1 : 10;
        int secPerRep = type == SequenceStepType.UNTIMED_REFS ? 1 : 60;

        model.getCurrentSequence().addStep(new SequenceStep(repetitions, secPerRep, type));
    }

    public void removeStep(int idx) {
        model.getCurrentSequence().removeStep(idx);
    }

    public void startPractice() {
        model.setRemainingSequenceStepsList(model.getCurrentSequence().getSteps());
        advanceToNextStep();
    }

    private void advanceToNextStep() {
        if (model.getRemainingSequenceStepsList().isEmpty()) {
            timer.jumpTo("end");
            model.setSessionFinished(true);
        } else {
            SequenceStep nextStep = model.remainingSequenceStepsListProperty().removeFirst();
            updateModel(nextStep);

            if (model.getCurrentSequenceStepType() != SequenceStepType.UNTIMED_REFS) {
                createTimer();
            }
            
            advanceInCurrentStep();
        }
    }

    public void advanceInCurrentStep() {
        //TODO: I need to ensure that numberOfRefs is always 1 for BREAK or do something else
        if (model.getCurrentRefNumber() < model.getCurrentSequenceStepRepetitions()) {
            if (model.getCurrentSequenceStepType() != SequenceStepType.UNTIMED_REFS) {
                timer.playFromStart();
            }
            if (model.getCurrentSequenceStepType() != SequenceStepType.BREAK) {
                setNextRef();
            }
            model.currentRefNumberProperty().set(model.getCurrentRefNumber()+1);
        } else {
            advanceToNextStep();
        }
    }

    private void setNextRef() {
        MatchableRef nextRef = getRandomRef();
        model.currentRefProperty().set(nextRef);
        model.getDrawnRefsList().add(nextRef);
        if (!model.getDuplicatesAllowed()) {
            model.getSessionRefList().remove(nextRef);
        }
    }

    private MatchableRef getRandomRef() {
        return model.getSessionRefList().get(getRandomRefNumber());
    }

    private int getRandomRefNumber() {
        return (int) (Math.random() * model.getSessionRefList().size());
    }

    private void updateModel(SequenceStep nextStep) {
        model.setCurrentSequenceStepRepetitions(nextStep.getRepetitions());
        model.setCurrentSequenceStepSecPerRep(nextStep.getSecPerRep());
        model.setCurrentSequenceStepType(nextStep.getType());
        model.setCurrentRefNumber(0);
    }

    private void createTimer() {
        ObservableList<KeyFrame> keyFrames = timer.getKeyFrames();
        keyFrames.clear();
        keyFrames.add(
            new KeyFrame(
                Duration.seconds(model.getCurrentSequenceStepSecPerRep()),
                e -> advanceInCurrentStep()
            )
        );
    }

    public void startTimer() { 
        if (timer == null) {
            System.out.println("No timer exists");
        } else if (timer.getStatus() == Status.RUNNING) {
            System.out.println("Timer is already running");
        } else {
            timer.play();
        }
    }

    public void pauseTimer() {
        if (timer == null) {
            System.out.println("No timer exists");
        } else if (timer.getStatus() != Status.RUNNING) {
            System.out.println("The timer isn't running");
        } else {
            timer.pause();
        }
    }

    public void stopTimer() {
        if (timer == null) {
            System.out.println("No timer exists");
        } else if (timer.getStatus() == Status.STOPPED) {
            System.out.println("The timer is already stopped");
        } else {
            timer.stop();
        }
    }

    public void resetPractice() {
        stopTimer();
        model.getDrawnRefsList().clear();
        model.getSessionRefList().setAll(model.getFullRefList());
        model.setCurrentRef(null);
        model.setCurrentRefNumber(0);
        model.setSessionFinished(false);
    }
}
