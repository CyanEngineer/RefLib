package com.cyaneer.reflib.model;

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

public class PracticeModel {

    //TODO: In the future, move the full poselist to the App model
    private final ListProperty<File> fullPoseList = new SimpleListProperty<File>(FXCollections.observableArrayList());
    private final ListProperty<File> sessionPoseList = new SimpleListProperty<File>(FXCollections.observableArrayList());
    private final ListProperty<File> drawnPosesList = new SimpleListProperty<File>(FXCollections.observableArrayList());
    private final ListProperty<SequenceStep> sequenceStepList = new SimpleListProperty<SequenceStep>(FXCollections.observableArrayList());
    private final ListProperty<SequenceStep> remainingSequenceStepsList = new SimpleListProperty<SequenceStep>(FXCollections.observableArrayList());
    private final BooleanProperty isDuplicatesAllowed = new SimpleBooleanProperty(false);
    private final ObjectProperty<Integer> currentSequenceStepRepetitions = new SimpleObjectProperty<Integer>(0);
    private final ObjectProperty<Integer> currentSequenceStepSecPerRep = new SimpleObjectProperty<Integer>(0);
    private final ObjectProperty<SequenceStepType> currentSequenceStepType = new SimpleObjectProperty<SequenceStepType>(SequenceStepType.TIMED_POSES);
    private final ObjectProperty<File> currentPose = new SimpleObjectProperty<File>(null);
    private final IntegerProperty currentPoseNumber = new SimpleIntegerProperty(0);
    private final ObjectProperty<Integer> elapsedSeconds = new SimpleObjectProperty<Integer>(0);
    private final ObjectProperty<Status> timerStatus = new SimpleObjectProperty<Status>(Status.STOPPED);
    private final BooleanProperty isSessionFinished = new SimpleBooleanProperty(false);

    public ObservableList<File> getFullPoseList() {
        return fullPoseList.get();
    }

    public ListProperty<File> fullPoseListProperty() {
        return fullPoseList;
    }

    public void setFullPoseList(List<File> fullPoseList) {
        this.fullPoseList.set(FXCollections.observableArrayList(fullPoseList));
    }

    public ObservableList<File> getSessionPoseList() {
        return sessionPoseList.get();
    }

    public ListProperty<File> sessionPoseListProperty() {
        return sessionPoseList;
    }

    public void setSessionPoseList(List<File> sessionPoseList) {
        this.sessionPoseList.set(FXCollections.observableArrayList(sessionPoseList));
    }

    public ObservableList<File> getDrawnPosesList() {
        return drawnPosesList.get();
    }

    public ListProperty<File> drawnPosesListProperty() {
        return drawnPosesList;
    }

    public void setDrawnPosesList(List<File> drawnPosesList) {
        this.drawnPosesList.set(FXCollections.observableArrayList(drawnPosesList));
    }

    public ObservableList<SequenceStep> getSequenceStepList() {
        return sequenceStepList.get();
    }

    public ListProperty<SequenceStep> sequenceStepListProperty() {
        return sequenceStepList;
    }

    public void setSequenceStepList(List<SequenceStep> sequenceStepList) {
        this.sequenceStepList.set(FXCollections.observableArrayList(sequenceStepList));
    }

    public ObservableList<SequenceStep> getRemainingSequenceStepsList() {
        return remainingSequenceStepsList.get();
    }

    public ListProperty<SequenceStep> remainingSequenceStepsListProperty() {
        return remainingSequenceStepsList;
    }

    public void setRemainingSequenceStepsList(List<SequenceStep> remainingSequenceStepsList) {
        this.remainingSequenceStepsList.set(FXCollections.observableArrayList(remainingSequenceStepsList));
    }

    public int getCurrentSequenceStepRepetitions() {
        return currentSequenceStepRepetitions.get();
    }

    public ObjectProperty<Integer> currentSequenceStepRepetitionsProperty() {
        return currentSequenceStepRepetitions;
    }

    public void setCurrentSequenceStepRepetitions(int repetitions) {
        currentSequenceStepRepetitions.set(repetitions);
    }

    public int getCurrentSequenceStepSecPerRep() {
        return currentSequenceStepSecPerRep.get();
    }

    public ObjectProperty<Integer> currentSequenceStepSecPerRepProperty() {
        return currentSequenceStepSecPerRep;
    }

    public void setCurrentSequenceStepSecPerRep(int secPerRep) {
        currentSequenceStepSecPerRep.set(secPerRep);
    }

    public SequenceStepType getCurrentSequenceStepType() {
        return currentSequenceStepType.get();
    }

    public ObjectProperty<SequenceStepType> currentSequenceStepTypeProperty() {
        return currentSequenceStepType;
    }

    public void setCurrentSequenceStepType(SequenceStepType sequenceStepType) {
        currentSequenceStepType.set(sequenceStepType);
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

    public int getElapsedSeconds() {
        return elapsedSeconds.get();
    }

    public ObjectProperty<Integer> currentElapsedSecondsProperty() {
        return elapsedSeconds;
    }

    public Status getTimerStatus() {
        return timerStatus.get();
    }

    public ObjectProperty<Status> timerStatusProperty() {
        return timerStatus;
    }

    public boolean getSessionFinished() {
        return isSessionFinished.get();
    }

    public BooleanProperty isSessionFinishedProperty() {
        return isSessionFinished;
    }

    public void setSessionFinished(boolean isSessionFinished) {
        this.isSessionFinished.set(isSessionFinished);
    }

    public void enterDebug() {
        System.out.println("Debug");
    }
}
