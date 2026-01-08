package com.cyaneer.reflib;

import java.io.File;
import java.util.List;

import com.cyaneer.reflib.model.PracticeModel;
import com.cyaneer.reflib.model.SequenceStep;
import com.cyaneer.reflib.model.SequenceStepType;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.collections.ObservableList;
import javafx.util.Duration;

public class PracticeInteractor {
    private PracticeModel model;
    private PracticeService service = new PracticeService();
    private Timeline timer = new Timeline();

    public PracticeInteractor(PracticeModel model) {
        this.model = model;
        model.timerStatusProperty().bind(timer.statusProperty());
        model.currentElapsedSecondsProperty().bind(timer.currentTimeProperty());
    }

    public void loadImages() {
        List<File> fullPoseList = service.loadImages();
        model.setFullPoseList(fullPoseList);
        resetPractice(); //TODO: In the future, do this when practice screen is chosen from main menu
    }

    public void loadSequence() {
        List<SequenceStep> sequenceStepList = service.loadSequence();
        model.setSequenceStepList(sequenceStepList);
    }

    public void startPractice() {
        model.setRemainingSequenceStepsList(model.getSequenceStepList());
        advanceToNextStep();
    }

    private void advanceToNextStep() {
        if (model.getRemainingSequenceStepsList().isEmpty()) {
            //TODO: End practice
        } else {
            SequenceStep practiceStep = model.getRemainingSequenceStepsList().getFirst();
            
            //TODO: updateModel(practiceStep);
            // Create the appropriate kind of timer
            // Set whatever should be shown on the practice screen
            // Start the timer
            // Pop the list somewhere
        }

        createTimer();
        advanceInCurrentStep();
    }

    private void advanceInCurrentStep() {
        if (model.getCurrentPoseNumber() < model.getNumberOfPoses()) {
            File nextPose = getRandomPose();
            model.currentPoseProperty().set(nextPose);
            model.currentPoseNumberProperty().set(model.getCurrentPoseNumber()+1);
            model.getDrawnPosesList().add(nextPose);
            if (!model.getDuplicatesAllowed()) {
                model.getSessionPoseList().remove(nextPose);
            }
            timer.playFromStart();
        } else {
            model.setSessionFinished(true);
            stopTimer();
            //TODO: advanceToNextStep();
        }
    }

    private File getRandomPose() {
        return model.getSessionPoseList().get(getRandomPoseNumber());
    }

    private int getRandomPoseNumber() {
        return (int) (Math.random() * model.getSessionPoseList().size());
    }

    private void updateModel(SequenceStep step) {
        if (step.getType() != SequenceStepType.PAUSE) {
            model.setNumberOfPoses(step.getRepetitions());
            model.setSecondsPerPose((int) step.getDuration().toSeconds());
            model.setCurrentPoseNumber(0);
        }
    }

    private void createTimer() {
        ObservableList<KeyFrame> keyFrames = timer.getKeyFrames();
        keyFrames.clear();
        keyFrames.add(
            new KeyFrame(
                Duration.seconds(model.getSecondsPerPose()),
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
        model.getDrawnPosesList().clear();
        model.getSessionPoseList().clear();
        model.getSessionPoseList().addAll(model.getFullPoseList());
        model.setCurrentPose(null);
        model.setCurrentPoseNumber(0);
        model.setSessionFinished(false);
    }
}
