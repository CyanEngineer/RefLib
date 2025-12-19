package com.cyaneer.reflib;

import java.io.File;
import java.util.List;

import javafx.animation.Animation.Status;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;

public class PracticeModel {

    private final ListProperty<File> poseList = new SimpleListProperty<File>(FXCollections.observableArrayList());
    private final BooleanProperty isDuplicatesAllowed = new SimpleBooleanProperty(false);
    private final IntegerProperty numberOfPoses = new SimpleIntegerProperty(15);
    private final IntegerProperty secondsPerPose = new SimpleIntegerProperty(60);
    private final ObjectProperty<File> currentPose = new SimpleObjectProperty<File>(null);
    private final IntegerProperty currentPoseNumber = new SimpleIntegerProperty(0);
    private final ObjectProperty<Duration> elapsedSeconds = new SimpleObjectProperty<Duration>(Duration.ZERO);
    private final ObjectProperty<Status> timerStatus = new SimpleObjectProperty<Status>(Status.STOPPED);

    public ObservableList<File> getPoseList() {
        return poseList.get();
    }

    public ListProperty<File> poseListProperty() {
        return poseList;
    }

    public void setPoseList(List<File> poseList) {
        this.poseList.set(FXCollections.observableArrayList(poseList));
    }

    public boolean getDuplicatesAllowed() {
        return isDuplicatesAllowed.get();
    }

    public BooleanProperty isDuplicatesAllowedProperty() {
        return isDuplicatesAllowed;
    }

    public void setDuplicatesAllowed(boolean isDuplicatesAllowed) {
        this.isDuplicatesAllowed.set(isDuplicatesAllowed);
    }

    public int getNumberOfPoses() {
        return numberOfPoses.get();
    }

    public IntegerProperty numberOfPosesProperty() {
        return numberOfPoses;
    }

    public void setNumberOfPoses(int numberOfPoses) {
        this.numberOfPoses.set(numberOfPoses);
    }

    public int getSecondsPerPose() {
        return secondsPerPose.get();
    }

    public IntegerProperty secondsPerPoseProperty() {
        return secondsPerPose;
    }

    public void setSecondsPerPose(int secondsPerPose) {
        this.secondsPerPose.set(secondsPerPose);
    }

    public File getcurrentPose() {
        return currentPose.get();
    }

    public ObjectProperty<File> currentPoseProperty() {
        return currentPose;
    }

    public void setCurrentPose(File currentPose) {
        this.currentPose.set(currentPose);
    }

    public int getCurrentPoseNumber() {
        return currentPoseNumber.get();
    }

    public IntegerProperty currentPoseNumberProperty() {
        return currentPoseNumber;
    }

    public void setCurrentPoseNumber(int currentPoseNumber) {
        this.currentPoseNumber.set(currentPoseNumber);
    }

    public Duration getElapsedSeconds() {
        return elapsedSeconds.get();
    }

    public ObjectProperty<Duration> currentElapsedSecondsProperty() {
        return elapsedSeconds;
    }

    public Status getTimerStatus() {
        return timerStatus.get();
    }

    public ObjectProperty<Status> timerStatusProperty() {
        return timerStatus;
    }
}
