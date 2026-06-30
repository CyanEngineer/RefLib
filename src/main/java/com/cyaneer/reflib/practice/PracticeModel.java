package com.cyaneer.reflib.practice;

import java.util.List;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.practice.domain.Sequence;
import com.cyaneer.reflib.practice.domain.SequenceStep;
import com.cyaneer.reflib.practice.domain.SequenceStepType;

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

    private final ListProperty<MatchableRef> fullRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final ListProperty<MatchableRef> sessionRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final ListProperty<MatchableRef> drawnRefsList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final ListProperty<Sequence> sequenceList = new SimpleListProperty<Sequence>(FXCollections.observableArrayList());
    private final ObjectProperty<Sequence> currentSequence = new SimpleObjectProperty<Sequence>(new Sequence());
    private final IntegerProperty currentSequenceTotalSeconds = new SimpleIntegerProperty(0);
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

    public PracticeModel() {
        currentSequence.addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) currentSequenceTotalSeconds.unbind();

            if (newVal != null) currentSequenceTotalSeconds.bind(newVal.totalSeconds());
            else currentSequenceTotalSeconds.set(0);
        });
    }

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

    public ObservableList<Sequence> getSequenceList() {
        return sequenceList.get();
    }

    public ListProperty<Sequence> sequenceListProperty() {
        return sequenceList;
    }

    public void setSequenceList(List<Sequence> sequenceList) {
        this.sequenceList.set(FXCollections.observableArrayList(sequenceList));
    }

    public Sequence getCurrentSequence() {
        return currentSequence.get();
    }

    public ObjectProperty<Sequence> currentSequenceProperty() {
        return currentSequence;
    }

    public void setCurrentSequence(Sequence sequence) {
        this.currentSequence.set(sequence);
    }

    public IntegerProperty currentSequenceTotalSecondsProperty() {
        return currentSequenceTotalSeconds;
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
