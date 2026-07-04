package com.cyaneer.reflib.practice;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.cyaneer.reflib.practice.domain.Sequence;
import com.cyaneer.reflib.practice.domain.SequenceStep;
import com.cyaneer.reflib.practice.domain.SequenceStepCell;
import com.cyaneer.reflib.practice.domain.SequenceStepType;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PracticePlanViewBuilder implements Builder<Region> {

    private final PracticeModel model;
    private final Consumer<SequenceStepType> addStepAction;
    private final Consumer<Integer> removeStepAction;
    private final Consumer<Sequence> setCurrentSequence;
    private final Consumer<Integer> saveCurrentSequenceAction;
    private final BiConsumer<Integer, Consumer<Sequence>> deleteCurrentSequenceAction;
    private final BiConsumer<String, Consumer<Sequence>> newSequenceAction;
    private final Runnable startAction;
    
    public PracticePlanViewBuilder(
        PracticeModel model,
        Consumer<SequenceStepType> addStepAction,
        Consumer<Integer> removeStepAction,
        Consumer<Sequence> setCurrentSequence,
        Consumer<Integer> saveCurrentSequenceAction,
        BiConsumer<Integer, Consumer<Sequence>> deleteCurrentSequenceAction,
        BiConsumer<String, Consumer<Sequence>> newSequenceAction,
        Runnable startAction
    ) {
        this.model = model;
        this.addStepAction = addStepAction;
        this.removeStepAction = removeStepAction;
        this.setCurrentSequence = setCurrentSequence;
        this.saveCurrentSequenceAction = saveCurrentSequenceAction;
        this.deleteCurrentSequenceAction = deleteCurrentSequenceAction;
        this.newSequenceAction = newSequenceAction;
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
        listView.itemsProperty().bind(Bindings.createObjectBinding(
            () -> {
                Sequence s = model.getCurrentSequence();
                return s == null ? FXCollections.<SequenceStep>observableArrayList() : s.getSteps();
            },
            model.currentSequenceProperty()
        ));

        return new BorderPane(
            listView,
            createSequenceSelector(),
            null,
            createSequenceControls(listView),
            null
        );
    }

    private SequenceStepCell createCell() {
        return new SequenceStepCell();
    }

    private Node createSequenceSelector() {
        ComboBox<Sequence> comboBox = new ComboBox<Sequence>();

        comboBox.itemsProperty().bind(model.sequenceListProperty());
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal != oldVal) {
                setCurrentSequence.accept(newVal);
            }
        });
        comboBox.setValue(model.getCurrentSequence());

        Button saveSequenceButton = new Button("Save sequence");
        saveSequenceButton.onActionProperty().set(e -> 
            saveCurrentSequenceAction.accept(comboBox.getSelectionModel().getSelectedIndex())
        );

        Button deleteSequenceButton = new Button("Delete sequence");
        deleteSequenceButton.onActionProperty().set(e ->
            deleteCurrentSequenceAction.accept(
                comboBox.getSelectionModel().getSelectedIndex(),
                (Sequence newSequence) -> comboBox.setValue(newSequence)
            )
        );

        TextField textField = new TextField();
        textField.setPromptText("New sequence name");

        Button newSequenceButton = new Button("New sequence");
        newSequenceButton.onActionProperty().set(e ->
            newSequenceAction.accept(
                textField.getText(),
                (Sequence newSequence) -> comboBox.setValue(newSequence)
            )
        );

        return new HBox(8,
            comboBox,
            saveSequenceButton,
            deleteSequenceButton,
            newSequenceButton,
            textField
        );
    }

    private Node createSequenceControls(ListView<SequenceStep> listView) {
        Button addTimedButton = new Button("Add timed refs");
        addTimedButton.setOnAction(e -> addStepAction.accept(SequenceStepType.TIMED_REFS));

        Button addUntimedButton = new Button("Add untimed refs");
        addUntimedButton.setOnAction(e -> addStepAction.accept(SequenceStepType.UNTIMED_REFS));

        Button addBreakButton = new Button("Add break");
        addBreakButton.setOnAction(e -> addStepAction.accept(SequenceStepType.BREAK));

        Button removeSelectedStepButton = new Button("Remove selected");
        removeSelectedStepButton.setOnAction(e -> 
            removeStepAction.accept(listView.getSelectionModel().getSelectedIndex())
        );

        return new HBox(8,
            addTimedButton,
            addUntimedButton,
            addBreakButton,
            removeSelectedStepButton
        );
    }

    private Node createButtons() {
        Button startButton = new Button("Start");

        Label timeLabel = new Label("");
        timeLabel.textProperty().bind(Bindings.createStringBinding(
            () -> "Practice duration: " + model.currentSequenceTotalSecondsProperty().intValue() + " seconds",
            model.currentSequenceTotalSecondsProperty())
        );

        startButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> model.getSessionRefList().size() == 0,
                model.sessionRefListProperty()));

        startButton.setOnAction(e -> startAction.run());
        HBox content = new HBox(8,
                new Button("Back"),
                timeLabel,
                startButton);
        content.setAlignment(Pos.CENTER);
        return content;
    }
}
