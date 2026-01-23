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
import javafx.util.converter.IntegerStringConverter;

public class SequenceStepCell extends ListCell<SequenceStep> {
    
    private final int MAX_SPINNER_VALUE = 1000;
    private SequenceStep model = new SequenceStep();
    private Region layout;

    public SequenceStepCell() {
        Node repetitionsSpinner = boundSpinner(model.repetitionsProperty());
        repetitionsSpinner.visibleProperty().bind(model.typeProperty().isNotEqualTo(SequenceStepType.BREAK));
        repetitionsSpinner.managedProperty().bind(repetitionsSpinner.visibleProperty());
        
        Node durationSpinner = boundSpinner(model.secPerRepProperty());
        durationSpinner.visibleProperty().bind(model.typeProperty().isNotEqualTo(SequenceStepType.UNTIMED_POSES));
        durationSpinner.managedProperty().bind(durationSpinner.visibleProperty());

        layout = new HBox(4,
            repetitionsSpinner,
            createContentLabel(),
            durationSpinner,
            createTimeUnitLabel()
        );
    }

    private Node createContentLabel() {
        Label label = new Label("");

        label.textProperty().bind(Bindings.createStringBinding(
            () -> {
                if (model.getType() == SequenceStepType.TIMED_POSES) {
                    return "poses of";
                } else if (model.getType() == SequenceStepType.UNTIMED_POSES) {
                    return "poses without timer";
                } else {
                    return "Break for";
                }
            },
            model.typeProperty()
        ));

        return label;
    }

    private Node createTimeUnitLabel() {
        Label label = new Label("");

        label.textProperty().bind(Bindings.createStringBinding(
            () -> {
                if (model.getType() == SequenceStepType.UNTIMED_POSES) {
                    return "";
                } else {
                    return "seconds"; //TODO: Support minutes
                }
            },
            model.typeProperty()
        ));

        return label;
    }

    private Node boundSpinner(ObjectProperty<Integer> property) {
        Spinner<Integer> spinner = new Spinner<Integer>(1, MAX_SPINNER_VALUE, 1);
        TextFormatter<Integer> positiveIntegerTextFormatter = new TextFormatter<Integer>(
            new PositiveIntegerStringConverter(), 
            1, 
            new PositiveIntegerFilter()
        );
        spinner.getEditor().setTextFormatter(positiveIntegerTextFormatter);
        spinner.setEditable(true);
        spinner.setPrefWidth(75);
        
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

        private final int MAX_SPINNER_DIGITS = ("" + MAX_SPINNER_VALUE).length();
        private final String regex = String.format("([0-9]{0,%d})?", MAX_SPINNER_DIGITS);

        @Override
        public Change apply(Change change) {
            if (change.getControlNewText().matches(regex)) {
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
