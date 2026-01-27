package com.cyaneer.reflib.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SequenceStep {
    
    private final ObjectProperty<Integer> repetitions = new SimpleObjectProperty<Integer>(1); // Ignore if type is PAUSE
    private final ObjectProperty<Integer> secPerRep = new SimpleObjectProperty<Integer>(1); // Ignore if type is UNTIMED_POSES
    private final ObjectProperty<SequenceStepType> type = new SimpleObjectProperty<SequenceStepType>(SequenceStepType.TIMED_POSES);
    private final IntegerBinding totalSeconds = Bindings.createIntegerBinding(
        () -> computeTotalSeconds(), 
        repetitions, secPerRep, type
    );

    public SequenceStep() {};

    public SequenceStep(int repetitions, int secPerRep, SequenceStepType type) {
        this.repetitions.set(repetitions);
        this.secPerRep.set(secPerRep);
        this.type.set(type);
    }

    public int getRepetitions() {
        return repetitions.get();
    }

    public ObjectProperty<Integer> repetitionsProperty() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions.set(repetitions);
    }

    public int getSecPerRep() {
        return secPerRep.get();
    }

    public ObjectProperty<Integer> secPerRepProperty() {
        return secPerRep;
    }

    public void setSecPerRep(Integer secPerRep) {
        this.secPerRep.set(secPerRep);
    }

    public SequenceStepType getType() {
        return type.get();
    }

    public ObjectProperty<SequenceStepType> typeProperty() {
        return type;
    }

    public void setType(SequenceStepType type) {
        this.type.set(type);
    }

    public IntegerBinding totalSeconds() {
        return totalSeconds;
    }

    private int computeTotalSeconds() {
        int repetitions = this.repetitions.get();
        int secPerRep = this.secPerRep.get();
        if (this.type.get() == SequenceStepType.BREAK) repetitions = 1;
        else if (this.type.get() == SequenceStepType.UNTIMED_POSES) secPerRep = 0;
        return repetitions * secPerRep;
    }

    public void bindTo(SequenceStep other) {
        repetitions.bindBidirectional(other.repetitions);
        secPerRep.bindBidirectional(other.secPerRep);
        type.bindBidirectional(other.type);
    }

    public void unbindFrom(SequenceStep other) {
        if (other != null) {
            repetitions.unbindBidirectional(other.repetitions);
            secPerRep.unbindBidirectional(other.secPerRep);
            type.unbindBidirectional(other.type);
        }
    }
}