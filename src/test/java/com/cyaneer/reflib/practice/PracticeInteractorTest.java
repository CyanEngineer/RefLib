package com.cyaneer.reflib.practice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.domain.SIFTMatchableRef;
import com.cyaneer.reflib.practice.domain.SequenceStepType;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.io.File;
import java.lang.IndexOutOfBoundsException;

public class PracticeInteractorTest {
    
    @Test
    public void testModelRefListUsesMasterRefList() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        new PracticeInteractor(model, masterRefList, isRefListLoaded);

        Assertions.assertEquals(model.getFullRefList().size(), 0);
        Assertions.assertEquals(masterRefList.size(), 0);

        masterRefList.add(new SIFTMatchableRef(new File("src/test/resources/testimage.png")));
        Assertions.assertEquals(model.getFullRefList().size(), 1);
        Assertions.assertEquals(masterRefList.size(), 1);

        masterRefList.remove(0);
        Assertions.assertEquals(model.getFullRefList().size(), 0);
        Assertions.assertEquals(masterRefList.size(), 0);
    }

    @Test
    public void settingIsRefsLoadedTrueLoadsRefsIntoSessionRefList() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        masterRefList.add(new SIFTMatchableRef(new File("src/test/resources/testimage.png")));

        new PracticeInteractor(model, masterRefList, isRefListLoaded);

        Assertions.assertEquals(model.getSessionRefList().size(), 0);
        Assertions.assertEquals(masterRefList.size(), 1);

        isRefListLoaded.set(true);

        Assertions.assertEquals(model.getSessionRefList().size(), 1);
        Assertions.assertEquals(masterRefList.size(), 1);
    }
    
    @Test
    public void testAddStep() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, masterRefList, isRefListLoaded);

        Assertions.assertEquals(model.getCurrentSequence().getSteps().size(), 0);

        interactor.addStep(SequenceStepType.TIMED_REFS);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().size(), 1);

        interactor.addStep(SequenceStepType.UNTIMED_REFS);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().size(), 2);

        interactor.addStep(SequenceStepType.BREAK);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().size(), 3);
    }

    @Test
    public void testAddTimedRefsStepHasDefaultValues() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, masterRefList, isRefListLoaded);

        interactor.addStep(SequenceStepType.TIMED_REFS);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getRepetitions(), 10);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getSecPerRep(), 60);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getType(), SequenceStepType.TIMED_REFS);
    }

    @Test
    public void testAddUntimedRefsStepHasDefaultValues() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, masterRefList, isRefListLoaded);

        interactor.addStep(SequenceStepType.UNTIMED_REFS);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getRepetitions(), 10);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getSecPerRep(), 1);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getType(), SequenceStepType.UNTIMED_REFS);
    }

    @Test
    public void testAddBreakStepHasDefaultValues() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, masterRefList, isRefListLoaded);

        interactor.addStep(SequenceStepType.BREAK);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getRepetitions(), 1);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getSecPerRep(), 60);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().get(0).getType(), SequenceStepType.BREAK);
    }

    @Test
    public void testRemoveStep() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, masterRefList, isRefListLoaded);

        interactor.addStep(SequenceStepType.TIMED_REFS);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().size(), 1);
        interactor.removeStep(0);
        Assertions.assertEquals(model.getCurrentSequence().getSteps().size(), 0);
    }

    @Test
    public void testRemoveStepWithInvalidIndex() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, masterRefList, isRefListLoaded);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> interactor.removeStep(0));
    }

    @Test
    public void testRemoveStepWithNegativeIndex() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, masterRefList, isRefListLoaded);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> interactor.removeStep(-1));
    }

    @Test
    public void testStartPractice() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, masterRefList, isRefListLoaded);

        interactor.startPractice();
        interactor.stopTimer();
    }
}