package com.cyaneer.reflib.practice.domain;

import org.junit.jupiter.api.Test;

public class SequenceTest {
    
    @Test
    public void testAddStepSuccessfullyAddsStep() {
        Sequence sequence = new Sequence();
        assert(sequence.getSteps().size() == 0);

        SequenceStep step = new SequenceStep();
        sequence.addStep(step);
        assert(sequence.getSteps().size() == 1);
        assert(sequence.getSteps().get(0) == step);
    }

    @Test
    public void testAddStepAddsStepAtGivenIndex() {
        Sequence sequence = new Sequence();

        SequenceStep step1 = new SequenceStep();
        SequenceStep step2 = new SequenceStep();
        SequenceStep step3 = new SequenceStep();
        
        sequence.addStep(step1);
        sequence.addStep(0, step2);
        assert(sequence.getSteps().get(0) == step2);
        assert(sequence.getSteps().get(1) == step1);

        sequence.addStep(1, step3);
        assert(sequence.getSteps().get(0) == step2);
        assert(sequence.getSteps().get(1) == step3);
        assert(sequence.getSteps().get(2) == step1);
    }

    @Test
    public void testRemoveStepRemovesStepAtGivenIndex() {
        Sequence sequence = new Sequence();

        SequenceStep step1 = new SequenceStep();
        SequenceStep step2 = new SequenceStep();
        SequenceStep step3 = new SequenceStep();
        
        sequence.addStep(0, step1);
        sequence.addStep(1, step2);
        sequence.addStep(2, step3);

        SequenceStep removedStep = sequence.removeStep(1);
        assert(removedStep == step2);
        assert(sequence.getSteps().get(0) == step1);
        assert(sequence.getSteps().get(1) == step3);
    }

    @Test
    public void testTotalSecondsIsUpdatedWhenStepIsAdded() {
        Sequence sequence = new Sequence();
        assert(sequence.getTotalSeconds() == 0);

        int reps1 = 10;
        int secPerRep1 = 60;
        SequenceStep step1 = new SequenceStep(reps1, secPerRep1, SequenceStepType.TIMED_REFS);
        sequence.addStep(step1);
        assert(sequence.getTotalSeconds() == reps1*secPerRep1);

        int reps2 = 10;
        int secPerRep2 = 60;
        SequenceStep step2 = new SequenceStep(reps2, secPerRep2, SequenceStepType.BREAK);
        sequence.addStep(step2);
        assert(sequence.getTotalSeconds() == reps1*secPerRep1 + secPerRep2);

        int reps3 = 10;
        int secPerRep3 = 60;
        SequenceStep step3 = new SequenceStep(reps3, secPerRep3, SequenceStepType.UNTIMED_REFS);
        sequence.addStep(step3);
        assert(sequence.getTotalSeconds() == reps1*secPerRep1 + secPerRep2 + 0);
    }

    @Test
    public void testTotalSecondsIsUpdatedWhenStepIsRemoved() {
        Sequence sequence = new Sequence();

        int reps = 10;
        int secPerRep = 60;
        SequenceStep step = new SequenceStep(reps, secPerRep, SequenceStepType.TIMED_REFS);
        sequence.addStep(step);
        assert(sequence.getTotalSeconds() == reps*secPerRep);

        sequence.removeStep(0);
        assert(sequence.getTotalSeconds() == 0);
    }

    @Test
    public void testTotalSecondsIsUpdatedWhenStepIsUpdated() {
        Sequence sequence = new Sequence();

        int reps1 = 10;
        int secPerRep1 = 60;
        SequenceStep step = new SequenceStep(reps1, secPerRep1, SequenceStepType.TIMED_REFS);
        sequence.addStep(step);
        assert(sequence.getTotalSeconds() == reps1*secPerRep1);

        int reps2 = 5;
        step.setRepetitions(reps2);
        assert(sequence.getTotalSeconds() == reps2*secPerRep1);

        int secPerRep2 = 30;
        step.setSecPerRep(secPerRep2);
        assert(sequence.getTotalSeconds() == reps2*secPerRep2);

        step.setType(SequenceStepType.BREAK);
        assert(sequence.getTotalSeconds() == secPerRep2);
    }
}
