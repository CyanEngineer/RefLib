package com.cyaneer.reflib.practice.domain;

import java.util.List;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Sequence {
    private ObjectProperty<String> name = new SimpleObjectProperty<String>("");
    private ListProperty<SequenceStep> steps = new SimpleListProperty<SequenceStep>(
        this,
        "steps",
        FXCollections.observableArrayList(step -> new Observable[] {step.totalSeconds()}) // List should invalidate both when elements are added/deleted (default) and when totalSeconds of a step changes (for the sake of totalSeconds)
    );
    private final IntegerBinding totalSeconds = Bindings.createIntegerBinding(
        () -> steps.stream().mapToInt(SequenceStep::getTotalSeconds).sum(),
        steps
    );

    public void addStep(SequenceStep step) {
        steps.add(step);
    }

    public void addStep(int i, SequenceStep step) {
        steps.add(i, step);
    }

    public SequenceStep removeStep(int i) {
        return steps.remove(i);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObjectProperty<String> nameProperty() {
        return name;
    }

    public ObservableList<SequenceStep> getSteps() {
        return steps.get();
    }

    public void setSteps(List<SequenceStep> steps) {
        this.steps.setAll(steps);
    }

    public ListProperty<SequenceStep> stepsProperty() {
        return steps;
    }

    public int getTotalSeconds() {
        return totalSeconds.get();
    }

    public IntegerBinding totalSeconds() {
        return totalSeconds;
    }
}
