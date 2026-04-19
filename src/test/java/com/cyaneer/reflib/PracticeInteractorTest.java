package com.cyaneer.reflib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.IndexOutOfBoundsException;

import com.cyaneer.reflib.model.PracticeModel;
import com.cyaneer.reflib.model.SequenceStepType;


public class PracticeInteractorTest {
    
    @Test
    public void testAddStep() {
        PracticeModel model  = new PracticeModel();
        PracticeInteractor interactor = new PracticeInteractor(model);
        Assertions.assertEquals(model.getSequenceStepList().size(), 0);

        interactor.addStep(SequenceStepType.TIMED_POSES);
        Assertions.assertEquals(model.getSequenceStepList().size(), 1);

        interactor.addStep(SequenceStepType.UNTIMED_POSES);
        Assertions.assertEquals(model.getSequenceStepList().size(), 2);

        interactor.addStep(SequenceStepType.BREAK);
        Assertions.assertEquals(model.getSequenceStepList().size(), 3);
    }

    @Test
    public void testAddTimedPosesStepHasDefaultValues() {
        PracticeModel model  = new PracticeModel();
        PracticeInteractor interactor = new PracticeInteractor(model);
        interactor.addStep(SequenceStepType.TIMED_POSES);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getRepetitions(), 10);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getSecPerRep(), 60);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getType(), SequenceStepType.TIMED_POSES);
    }

    @Test
    public void testAddUntimedPosesStepHasDefaultValues() {
        PracticeModel model  = new PracticeModel();
        PracticeInteractor interactor = new PracticeInteractor(model);
        interactor.addStep(SequenceStepType.UNTIMED_POSES);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getRepetitions(), 10);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getSecPerRep(), 1);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getType(), SequenceStepType.UNTIMED_POSES);
    }

    @Test
    public void testAddBreakStepHasDefaultValues() {
        PracticeModel model  = new PracticeModel();
        PracticeInteractor interactor = new PracticeInteractor(model);
        interactor.addStep(SequenceStepType.BREAK);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getRepetitions(), 1);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getSecPerRep(), 60);
        Assertions.assertEquals(model.getSequenceStepList().get(0).getType(), SequenceStepType.BREAK);
    }

    @Test
    public void testRemoveStep() {
        PracticeModel model = new PracticeModel();
        PracticeInteractor interactor = new PracticeInteractor(model);
        interactor.addStep(SequenceStepType.TIMED_POSES);
        Assertions.assertEquals(model.getSequenceStepList().size(), 1);
        interactor.removeStep(0);
        Assertions.assertEquals(model.getSequenceStepList().size(), 0);
    }

    @Test
    public void testRemoveStepWithInvalidIndex() {
        PracticeModel model = new PracticeModel();
        PracticeInteractor interactor = new PracticeInteractor(model);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> interactor.removeStep(0));
    }

    @Test
    public void testRemoveStepWithNegativeIndex() {
        PracticeModel model = new PracticeModel();
        PracticeInteractor interactor = new PracticeInteractor(model);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> interactor.removeStep(-1));
    }
}