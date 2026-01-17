package com.cyaneer.reflib.viewBuilder;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

import com.cyaneer.reflib.model.PracticeModel;
import com.cyaneer.reflib.model.SequenceStep;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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

    private Region createCenter() {
        ListView<SequenceStep> listView = new ListView<>();
        listView.setCellFactory(lv -> createCell());
        listView.itemsProperty().bind(model.sequenceStepListProperty()); // TODO: Fix haha
        System.out.println(listView.getItems().getFirst().getType().name());
        return listView;
    }

    private SequenceStepCell createCell() {
        return new SequenceStepCell();
    }

    // Keeping this as inspiration for editable SequenceStepCells
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

    private Node createButtons() {
        Button startButton = new Button("Start");
        startButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> model.getSessionPoseList().size() == 0,
                model.sessionPoseListProperty()));

        startButton.setOnAction(e -> startAction.run());
        HBox content = new HBox(8,
                new Button("Back"),
                new Label("Practice time: "),
                new Label("???"), //TODO: Calculate this somehow
                startButton);
        content.setAlignment(Pos.CENTER);
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
