package com.cyaneer.reflib.viewBuilder;

import java.util.function.UnaryOperator;

import com.cyaneer.reflib.model.SequenceStep;
import com.cyaneer.reflib.model.SequenceStepType;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.converter.IntegerStringConverter;

public class SequenceStepCell extends ListCell<SequenceStep> {
    
    private SequenceStep model = new SequenceStep();
    private Region layout;

    public SequenceStepCell() {
        Node timedPoseCell = createTimedPoseCell();
        timedPoseCell.visibleProperty().bind(model.typeProperty().isEqualTo(SequenceStepType.TIMED_POSES));
        Node untimedPoseCell = createUntimedPoseCell();
        untimedPoseCell.visibleProperty().bind(model.typeProperty().isEqualTo(SequenceStepType.UNTIMED_POSES));
        Node breakCell = createBreakCell();
        breakCell.visibleProperty().bind(model.typeProperty().isEqualTo(SequenceStepType.BREAK));
        
        layout = new StackPane(
            timedPoseCell,
            untimedPoseCell,
            breakCell
        );
    }

    private Node createTimedPoseCell() {
        Node repetitionsSpinner = boundSpinner(model.repetitionsProperty());
        Label label = new Label();
        label.textProperty().bind(Bindings.createStringBinding(
            () -> model.getRepetitions() + " poses of " + model.getSecPerRep() + " sec",
            model.repetitionsProperty(), model.secPerRepProperty())
        );
        return new HBox(repetitionsSpinner, label);
    }

    private Node createUntimedPoseCell() {
        Label label = new Label();
        label.textProperty().bind(Bindings.createStringBinding(
            () -> model.getRepetitions() + " untimed poses",
            model.repetitionsProperty())
        );
        return label;
    }

    private Node createBreakCell() {
        Label label = new Label();
        label.textProperty().bind(Bindings.createStringBinding(
            () -> model.getSecPerRep() + " seconds break",
            model.secPerRepProperty())
        );
        return label;
    }

    private Node boundSpinner(ObjectProperty<Integer> property) {
        Spinner<Integer> spinner = new Spinner<Integer>(1, 1000000, 1);
        TextFormatter<Integer> positiveIntegerTextFormatter = new TextFormatter<Integer>(
            new PositiveIntegerStringConverter(), 
            1, 
            new PositiveIntegerFilter()
        );
        spinner.getEditor().setTextFormatter(positiveIntegerTextFormatter);
        spinner.setEditable(true);
        
        positiveIntegerTextFormatter.valueProperty().bindBidirectional(property);
        
        return spinner;
    }

    private class PositiveIntegerStringConverter extends IntegerStringConverter {

        @Override
        public Integer fromString(String value) {
            Integer result = super.fromString(value);
            if (result == null) {
                throw new RuntimeException("Empty value");
            } else if (result < 1) {
                throw new RuntimeException("Non-positive value");
            }
            return result.intValue();
        }

        @Override
        public String toString(Integer value) {
            if (value < 1) {
                return "1";
            }
            return super.toString(value);
        }
    }

    private class PositiveIntegerFilter implements UnaryOperator<TextFormatter.Change> {

        @Override
        public Change apply(Change change) {
            if (change.getControlNewText().matches("[0-9]*")) {
                return change;
            }
            return null;
        }

    }
    
    @Override
    public void updateItem(SequenceStep item, boolean isEmpty) {
        model.unbindFrom(this.getItem());
        super.updateItem(item, isEmpty);
        if (!isEmpty && (item != null)) {
            model.bindTo(item);
            setGraphic(layout);
            setText(null);
        } else {
            setGraphic(null);
            setText(null);
        }
    }
}
