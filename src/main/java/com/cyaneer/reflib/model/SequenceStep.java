package com.cyaneer.reflib.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

public class SequenceStep {
    
    private final IntegerProperty repetitions = new SimpleIntegerProperty(1); // Ignore if type is PAUSE
    private final ObjectProperty<Duration> duration = new SimpleObjectProperty<Duration>(Duration.INDEFINITE); // Ignore if type is UNTIMED_POSES
    private final ObjectProperty<SequenceStepType> type = new SimpleObjectProperty<SequenceStepType>(SequenceStepType.UNTIMED_POSES);

    public SequenceStep() {};

    public SequenceStep(int repetitions, Duration duration, SequenceStepType type) {
        this.repetitions.set(repetitions);
        this.duration.set(duration);
        this.type.set(type);
    }

    public int getRepetitions() {
        return repetitions.get();
    }

    public IntegerProperty repetitionsProperty() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions.set(repetitions);
    }

    public Duration getDuration() {
        return duration.get();
    }

    public ObjectProperty<Duration> durationProperty() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration.set(duration);
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

    public void bindTo(SequenceStep other) {
        repetitions.bind(other.repetitions);
        duration.bind(other.duration);
        type.bind(other.type);
    }

    public void unbind() {
        repetitions.unbind();
        duration.unbind();
        type.unbind();
    }
}