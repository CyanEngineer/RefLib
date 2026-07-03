package com.cyaneer.reflib.practice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.domain.SIFTMatchableRef;
import com.cyaneer.reflib.practice.domain.Sequence;
import com.cyaneer.reflib.practice.domain.SequenceStep;
import com.cyaneer.reflib.practice.domain.SequenceStepType;
import com.cyaneer.reflib.practice.repository.SequenceRepository;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.io.File;
import java.io.IOException;
import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PracticeInteractorTest {
    
    @Test
    public void testModelRefListUsesMasterRefList() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

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

        new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

        Assertions.assertEquals(model.getSessionRefList().size(), 0);
        Assertions.assertEquals(masterRefList.size(), 1);

        isRefListLoaded.set(true);

        Assertions.assertEquals(model.getSessionRefList().size(), 1);
        Assertions.assertEquals(masterRefList.size(), 1);
    }

    @Test
    public void testSetCurrentSequenceSetsACopyOfSequence() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

        Sequence sequence = new Sequence();
        sequence.setName("Test");
        sequence.addStep(new SequenceStep(10, 60, SequenceStepType.TIMED_REFS));
        model.getSequenceList().add(sequence);
        assert(model.getCurrentSequence().getName() != sequence.getName());
        assert(model.getCurrentSequence().getSteps().size() != sequence.getSteps().size());

        interactor.setCurrentSequence(sequence);
        Sequence currentSequence = model.getCurrentSequence();
        assert(currentSequence != sequence);
        assert(currentSequence.getName().equals(sequence.getName()));
        assert(currentSequence.getSteps() != sequence.getSteps());
        assert(currentSequence.getSteps().size() == 1);
        assert(currentSequence.getSteps().get(0) != sequence.getSteps().get(0));
        assert(currentSequence.getSteps().get(0).getRepetitions() == sequence.getSteps().get(0).getRepetitions());
        assert(currentSequence.getSteps().get(0).getSecPerRep() == sequence.getSteps().get(0).getSecPerRep());
        assert(currentSequence.getSteps().get(0).getType() == sequence.getSteps().get(0).getType());
    }

    @Test
    public void testChangesToCurrentSequenceDontChangeOriginal() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

        Sequence sequence = new Sequence();
        sequence.setName("Test");
        sequence.addStep(new SequenceStep(10, 60, SequenceStepType.TIMED_REFS));
        model.getSequenceList().add(sequence);

        interactor.setCurrentSequence(sequence);
        Sequence currentSequence = model.getCurrentSequence();

        currentSequence.setName("Modified");
        assert(!currentSequence.getName().equals(sequence.getName()));

        currentSequence.getSteps().get(0).setRepetitions(1);
        assert(currentSequence.getSteps().get(0).getRepetitions() != sequence.getSteps().get(0).getRepetitions());
        
        currentSequence.getSteps().get(0).setSecPerRep(6);
        assert(currentSequence.getSteps().get(0).getSecPerRep() != sequence.getSteps().get(0).getSecPerRep());

        currentSequence.getSteps().get(0).setType(SequenceStepType.BREAK);
        assert(currentSequence.getSteps().get(0).getType() != sequence.getSteps().get(0).getType());
    }

    @Test
    public void testSaveSequenceOverwritesOriginalSequence() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        SequenceRepository repository = new SequenceRepository() {
            @Override
            public void saveSequences(List<Sequence> sequences) throws IOException {}

            @Override
            public List<Sequence> loadSequences() throws IOException {
                return new ArrayList<>();
            }
        };

        PracticeInteractor interactor = new PracticeInteractor(model, repository, masterRefList, isRefListLoaded);

        Sequence sequence = new Sequence();
        sequence.setName("Test");
        sequence.addStep(new SequenceStep(10, 60, SequenceStepType.TIMED_REFS));
        model.getSequenceList().add(sequence);

        interactor.setCurrentSequence(sequence);
        Sequence currentSequence = model.getCurrentSequence();

        currentSequence.setName("Modified");
        currentSequence.getSteps().get(0).setRepetitions(1);
        currentSequence.getSteps().get(0).setSecPerRep(6);
        currentSequence.getSteps().get(0).setType(SequenceStepType.BREAK);
        
        interactor.saveCurrentSequence(0);

        assert(currentSequence.getName().equals(sequence.getName()));
        assert(currentSequence.getSteps().get(0).getRepetitions() == sequence.getSteps().get(0).getRepetitions());
        assert(currentSequence.getSteps().get(0).getSecPerRep() == sequence.getSteps().get(0).getSecPerRep());
        assert(currentSequence.getSteps().get(0).getType() == sequence.getSteps().get(0).getType());
    }

    @Test
    public void testDeleteSequenceDeletesSequence() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        SequenceRepository repository = new SequenceRepository() {
            @Override
            public void saveSequences(List<Sequence> sequences) throws IOException {}

            @Override
            public List<Sequence> loadSequences() throws IOException {
                return new ArrayList<>();
            }
        };

        PracticeInteractor interactor = new PracticeInteractor(model, repository, masterRefList, isRefListLoaded);

        Sequence sequence = new Sequence();
        sequence.setName("Test");
        sequence.addStep(new SequenceStep(10, 60, SequenceStepType.TIMED_REFS));
        model.getSequenceList().add(sequence);
        Sequence blank = new Sequence();
        model.getSequenceList().add(blank);

        interactor.setCurrentSequence(sequence);

        interactor.deleteCurrentSequence(0, (seq) -> {});
        assert(model.getSequenceList().size() == 1);
        assert(model.getSequenceList().get(0).getName().equals(blank.getName()));
        assert(model.getSequenceList().get(0).getSteps().size() == blank.getSteps().size());
    }

    @Test
    public void testCreateSequenceCreatesNewSequence() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

        assert(model.getSequenceList().size() == 0);

        String name = "Test";
        interactor.createNewSequence(name, (newSequence) -> {});
        assert(model.getSequenceList().size() == 1);
        assert(model.getSequenceList().get(0).getName().equals(name));
    }
    
    @Test
    public void testAddStep() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

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

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

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

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

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

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

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

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

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

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> interactor.removeStep(0));
    }

    @Test
    public void testRemoveStepWithNegativeIndex() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> interactor.removeStep(-1));
    }

    @Test
    public void testStartPractice() {
        PracticeModel model = new PracticeModel();
        ListProperty<MatchableRef> masterRefList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
        ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);

        PracticeInteractor interactor = new PracticeInteractor(model, null, masterRefList, isRefListLoaded);

        interactor.startPractice();
        interactor.stopTimer();
    }
}