package com.cyaneer.reflib;

import java.io.File;
import java.util.List;

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
        List<File> poseList = service.loadImages();
        model.setPoseList(poseList);
    }

    public void setNextImage() {
        File nextPose = model.getPoseList().get(getRandomPoseNumber());
        model.currentPoseProperty().set(nextPose);
        model.currentPoseNumberProperty().set(model.getCurrentPoseNumber()+1);
    }

    private int getRandomPoseNumber() {
        return (int) (Math.random() * model.getPoseList().size());
    }

    private void createTimer() {
        ObservableList<KeyFrame> keyFrames = timer.getKeyFrames();
        keyFrames.clear();
        keyFrames.add(new KeyFrame(
                Duration.seconds(model.getSecondsPerPose()),
                e -> {
                    if (model.getCurrentPoseNumber() < model.getNumberOfPoses()) {
                        setNextImage();
                    }
                }
            )
        );
        timer.setCycleCount(model.getNumberOfPoses());
    }

    public void startTimer() {
        System.out.println("Timer started??");
        if (timer == null || timer.getStatus() == Status.STOPPED) {
            createTimer();
            model.currentPoseNumberProperty().set(0);
            setNextImage();
        } else if (timer.getStatus() == Status.RUNNING) {
            System.out.println("Timer is already running");
            return;
        }
        timer.play();
    }

    public void pauseTimer() {
        if (timer == null) {
            System.out.println("No timer exists");
        } else if (timer.getStatus() == Status.RUNNING) {
            timer.pause();
        } else {
            System.out.println("The timer isn't running");
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
}
