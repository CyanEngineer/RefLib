package com.cyaneer.reflib.practice.domain;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

public class SequenceStepTest {

    @Test
    public void testTotalSeconds() {
        int reps = 10;
        int secPerRep = 60;
        SequenceStep step = new SequenceStep(reps, secPerRep, SequenceStepType.TIMED_REFS);
        Assertions.assertEquals(step.getTotalSeconds(), reps*secPerRep);

        int newReps = 5;
        step.setRepetitions(newReps);
        Assertions.assertEquals(step.getTotalSeconds(), newReps*secPerRep);

        int newSecPerRep = 30;
        step.setSecPerRep(newSecPerRep);
        Assertions.assertEquals(step.getTotalSeconds(), newReps*newSecPerRep);
    }

    @Test
    public void testTotalSecondsIgnoresRepsForBreak() {
        int reps = 10;
        int secPerRep = 60;
        SequenceStep step = new SequenceStep(reps, secPerRep, SequenceStepType.BREAK);
        Assertions.assertEquals(step.getTotalSeconds(), secPerRep);
    }

    @Test
    public void testTotalSecondsIgnoresSecPerRepForUntimedRefs() {
        int reps = 10;
        int secPerRep = 60;
        SequenceStep step = new SequenceStep(reps, secPerRep, SequenceStepType.UNTIMED_REFS);
        Assertions.assertEquals(step.getTotalSeconds(), 0);
    }

    @Test
    public void testTotalSecondsAfterChangingType() {
        int reps = 10;
        int secPerRep = 60;
        SequenceStep step = new SequenceStep(reps, secPerRep, SequenceStepType.TIMED_REFS);
        Assertions.assertEquals(step.getTotalSeconds(), reps*secPerRep);

        step.setType(SequenceStepType.BREAK);
        Assertions.assertEquals(step.getTotalSeconds(), secPerRep);

        step.setType(SequenceStepType.UNTIMED_REFS);
        Assertions.assertEquals(step.getTotalSeconds(), 0);
    }

    @Test
    public void testBindTo() {
        SequenceStep step1 = new SequenceStep(10, 60, SequenceStepType.TIMED_REFS);
        SequenceStep step2 = new SequenceStep();
        step2.bindTo(step1);
        Assertions.assertEquals(step2.getRepetitions(), step1.getRepetitions());
        Assertions.assertEquals(step2.getSecPerRep(), step1.getSecPerRep());
        Assertions.assertEquals(step2.getType(), step1.getType());

        step2.setRepetitions(5);
        step2.setSecPerRep(30);
        step2.setType(SequenceStepType.UNTIMED_REFS);
        Assertions.assertEquals(step1.getRepetitions(), 5);
        Assertions.assertEquals(step1.getSecPerRep(), 30);
        Assertions.assertEquals(step1.getType(), SequenceStepType.UNTIMED_REFS);
    }

    @Test
    public void testUnbindFrom() {
        SequenceStep step1 = new SequenceStep(10, 60, SequenceStepType.TIMED_REFS);
        SequenceStep step2 = new SequenceStep();
        step2.bindTo(step1);
        step2.unbindFrom(step1);
        step1.setRepetitions(5);
        step1.setSecPerRep(30);
        step1.setType(SequenceStepType.UNTIMED_REFS);
        Assertions.assertEquals(step2.getRepetitions(), 10);
        Assertions.assertEquals(step2.getSecPerRep(), 60);
        Assertions.assertEquals(step2.getType(), SequenceStepType.TIMED_REFS);
    }
}
