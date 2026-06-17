package com.cyaneer.reflib.practice;

import java.util.List;

import com.cyaneer.reflib.domain.MatchableRef;

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

    //TODO: In the future, move the full reflist to the App model
    private final ListProperty<MatchableRef> fullRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final ListProperty<MatchableRef> sessionRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final ListProperty<MatchableRef> drawnRefsList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final ListProperty<SequenceStep> sequenceStepList = new SimpleListProperty<SequenceStep>(FXCollections.observableArrayList());
    private final ListProperty<SequenceStep> remainingSequenceStepsList = new SimpleListProperty<SequenceStep>(FXCollections.observableArrayList());
    private final BooleanProperty isDuplicatesAllowed = new SimpleBooleanProperty(false);
    private final ObjectProperty<Integer> currentSequenceStepRepetitions = new SimpleObjectProperty<Integer>(0);
    private final ObjectProperty<Integer> currentSequenceStepSecPerRep = new SimpleObjectProperty<Integer>(0);
    private final ObjectProperty<SequenceStepType> currentSequenceStepType = new SimpleObjectProperty<SequenceStepType>(SequenceStepType.TIMED_REFS);
    private final ObjectProperty<MatchableRef> currentRef = new SimpleObjectProperty<MatchableRef>(null);
    private final IntegerProperty currentRefNumber = new SimpleIntegerProperty(0);
    private final ObjectProperty<Integer> elapsedSeconds = new SimpleObjectProperty<Integer>(0);
    private final ObjectProperty<Status> timerStatus = new SimpleObjectProperty<Status>(Status.STOPPED);
    private final BooleanProperty isSessionFinished = new SimpleBooleanProperty(false);

    public ObservableList<MatchableRef> getFullRefList() {
        return fullRefList.get();
    }

    public ListProperty<MatchableRef> fullRefListProperty() {
        return fullRefList;
    }

    public ObservableList<MatchableRef> getSessionRefList() {
        return sessionRefList.get();
    }

    public ListProperty<MatchableRef> sessionRefListProperty() {
        return sessionRefList;
    }

    public void setSessionRefList(List<MatchableRef> sessionRefList) {
        this.sessionRefList.set(FXCollections.observableArrayList(sessionRefList));
    }

    public ObservableList<MatchableRef> getDrawnRefsList() {
        return drawnRefsList.get();
    }

    public ListProperty<MatchableRef> drawnRefsListProperty() {
        return drawnRefsList;
    }

    public void setDrawnRefsList(List<MatchableRef> drawnRefsList) {
        this.drawnRefsList.set(FXCollections.observableArrayList(drawnRefsList));
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

    public MatchableRef getcurrentRef() {
        return currentRef.get();
    }

    public ObjectProperty<MatchableRef> currentRefProperty() {
        return currentRef;
    }

    public void setCurrentRef(MatchableRef currentRef) {
        this.currentRef.set(currentRef);
    }

    public int getCurrentRefNumber() {
        return currentRefNumber.get();
    }

    public IntegerProperty currentRefNumberProperty() {
        return currentRefNumber;
    }

    public void setCurrentRefNumber(int currentRefNumber) {
        this.currentRefNumber.set(currentRefNumber);
    }

    public int getElapsedSeconds() {
        return elapsedSeconds.get();
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds.set(elapsedSeconds);
    }

    public ObjectProperty<Integer> elapsedSecondsProperty() {
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
