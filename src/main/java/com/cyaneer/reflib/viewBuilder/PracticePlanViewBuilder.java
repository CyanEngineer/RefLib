package com.cyaneer.reflib.viewBuilder;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

import com.cyaneer.reflib.PracticeModel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.converter.IntegerStringConverter;

public class PracticePlanViewBuilder implements Builder<Region> {

    private final PracticeModel model;
    private final Runnable startAction;
    private final ArrayList<ObjectProperty> weakListenerList = new ArrayList<>(); //There has to be a nicer way to save weak listeners...

    public PracticePlanViewBuilder(PracticeModel model, Runnable startAction) {
        this.model = model;
        this.startAction = startAction;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeadingLabel("Plan your practice"));
        borderPane.setCenter(createCenter());
        borderPane.setBottom(createButtons());
        return borderPane;
    }

    private Node createHeadingLabel(String string) {
        return new Label(string);
    }

    private Node createCenter() {

        VBox content = new VBox(8,
                createSequenceInput(),
                createOptions()
            );

        content.setAlignment(Pos.CENTER);
        return content;
    }

    private Node createSequenceInput() {

        HBox content = new HBox(8,
                new Label("I want to draw"),
                createNPosesField(),
                new Label("poses of"),
                createNSecondsField(),
                new Label("seconds each"));

        content.setAlignment(Pos.CENTER);
        return content;
    }

    private Node createNPosesField() {
        return boundTextField(model.numberOfPosesProperty());
    }

    private Node createNSecondsField() {
        return boundTextField(model.secondsPerPoseProperty());
    }

    private Node boundTextField(IntegerProperty property) {
        Spinner<Integer> spinner = new Spinner<Integer>(1, 1000000, 1);
        TextFormatter<Integer> positiveIntegerTextFormatter = new TextFormatter<Integer>(
            new PositiveIntegerStringConverter(), 
            1, 
            new PositiveIntegerFilter()
        );
        spinner.getEditor().setTextFormatter(positiveIntegerTextFormatter);
        spinner.setEditable(true);
        
        ObjectProperty<Integer> obProperty = property.asObject();
        weakListenerList.add(obProperty);
        
        positiveIntegerTextFormatter.valueProperty().bindBidirectional(obProperty);
        return spinner;
    }

    private Node createOptions() {
        CheckBox checkBox = new CheckBox("Allow duplicates");
        checkBox.selectedProperty().bindBidirectional(model.isDuplicatesAllowedProperty());

        return checkBox;
    }

    private Node createButtons() {
        Button startButton = new Button("Start");
        startButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> model.getSessionPoseList().size() == 0,
                model.sessionPoseListProperty()));

        startButton.setOnAction(e -> startAction.run());
        HBox content = new HBox(8,
                new Button("Back"),
                new Label("Practice time: "),
                createTotalTimeLabel(),
                startButton);
        content.setAlignment(Pos.CENTER);
        return content;
    }

    private Node createTotalTimeLabel() {
        Label content = new Label();

        StringProperty totalTimeProperty = new SimpleStringProperty("");
        totalTimeProperty.bind(Bindings.createStringBinding(() -> {
            int total = model.getNumberOfPoses() * model.getSecondsPerPose();
            return String.format("%02d:%02d:%02d", total / 3600, (total % 3600) / 60, total % 60);
        }, model.numberOfPosesProperty(), model.secondsPerPoseProperty()));

        content.textProperty().bind(totalTimeProperty);
        return content;
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
}
