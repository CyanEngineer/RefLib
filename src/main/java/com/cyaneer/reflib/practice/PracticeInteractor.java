package com.cyaneer.reflib.practice;

import java.util.List;

import com.cyaneer.reflib.PracticeService;
import com.cyaneer.reflib.domain.MatchableRef;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.util.Duration;

public class PracticeInteractor {
    private PracticeModel model;
    private PracticeService service = new PracticeService();
    private Timeline timer = new Timeline();

    public PracticeInteractor(PracticeModel model, BooleanProperty isRefListLoaded) {
        this.model = model;

        isRefListLoaded.addListener((obs, oldValue, newValue) -> {
            if (newValue) resetPractice();
        });

        model.timerStatusProperty().bind(timer.statusProperty());
        //TODO: I can't imagine that it isn't hella inefficient to have a binding that's called every ms...
        ObjectBinding<Integer> currentTimeBinding = Bindings.createObjectBinding(
            () -> (int) timer.currentTimeProperty().get().toSeconds(),
            timer.currentTimeProperty()
        );
        model.currentElapsedSecondsProperty().bind(currentTimeBinding);
    }

    public void loadSequence() {
        List<SequenceStep> sequenceStepList = service.loadSequence();
        model.setSequenceStepList(sequenceStepList);
    }

    public void addStep(SequenceStepType type) {
        int repetitions = type == SequenceStepType.BREAK ? 1 : 10;
        int secPerRep = type == SequenceStepType.UNTIMED_REFS ? 1 : 60;

        model.sequenceStepListProperty().add(new SequenceStep(repetitions, secPerRep, type));
    }

    public void removeStep(int idx) {
        model.sequenceStepListProperty().remove(idx);
    }

    public void startPractice() {
        model.setRemainingSequenceStepsList(model.getSequenceStepList());
        advanceToNextStep();
    }

    private void advanceToNextStep() {
        if (model.getRemainingSequenceStepsList().isEmpty()) {
            timer.jumpTo("end"); //TODO: Verify that this doesn't break anything
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
