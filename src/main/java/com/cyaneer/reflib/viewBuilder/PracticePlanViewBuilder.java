package com.cyaneer.reflib.viewBuilder;

import java.util.function.Consumer;

import com.cyaneer.reflib.model.PracticeModel;
import com.cyaneer.reflib.model.SequenceStep;
import com.cyaneer.reflib.model.SequenceStepType;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PracticePlanViewBuilder implements Builder<Region> {

    private final PracticeModel model;
    private final Consumer<SequenceStepType> addStepAction;
    private final Consumer<Integer> removeStepAction;
    private final Runnable startAction;
    
    public PracticePlanViewBuilder(
        PracticeModel model,
        Consumer<SequenceStepType> addStepAction,
        Consumer<Integer> removeStepAction,
        Runnable startAction
    ) {
        this.model = model;
        this.addStepAction = addStepAction;
        this.removeStepAction = removeStepAction;
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
        listView.setItems(model.getSequenceStepList());

        return new BorderPane(
            listView,
            null,
            null,
            createSequenceControls(listView),
            null
        );
    }

    private SequenceStepCell createCell() {
        return new SequenceStepCell();
    }

    private Node createSequenceControls(ListView<SequenceStep> listView) {
        Button addTimedButton = new Button("Add timed poses");
        addTimedButton.setOnAction(e -> addStepAction.accept(SequenceStepType.TIMED_POSES));

        Button addUntimedButton = new Button("Add untimed poses");
        addUntimedButton.setOnAction(e -> addStepAction.accept(SequenceStepType.UNTIMED_POSES));

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
        updateTimeLabelDeps(timeLabel);

        model.sequenceStepListProperty().addListener(
            (ob, oldValue, newValue) -> updateTimeLabelDeps(timeLabel)
        );

        startButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> model.getSessionPoseList().size() == 0,
                model.sessionPoseListProperty()));

        startButton.setOnAction(e -> startAction.run());
        HBox content = new HBox(8,
                new Button("Back"),
                timeLabel,
                startButton);
        content.setAlignment(Pos.CENTER);
        return content;
    }

    private void updateTimeLabelDeps(Label timeLabel) {

        ObservableList<SequenceStep> sequenceStepList = model.getSequenceStepList();
        Observable[] deps = new Observable[sequenceStepList.size()];
        for (int i=0; i< sequenceStepList.size(); i++) {
            deps[i] = sequenceStepList.get(i).totalSeconds();
        }

        timeLabel.textProperty().unbind();
        timeLabel.textProperty().bind(Bindings.createStringBinding(
            () -> createTimeLabelString(), 
            deps
        ));
    }

    private String createTimeLabelString() {
        int drawingSeconds = 0;
        int breakSeconds = 0;
        for (SequenceStep ss : model.getSequenceStepList()) {
            if (ss.getType() == SequenceStepType.TIMED_POSES) {
                drawingSeconds += ss.totalSeconds().get();
            } else if (ss.getType() == SequenceStepType.BREAK) {
                breakSeconds += ss.totalSeconds().get();
            }
        }

        String string = "";
        if (drawingSeconds > 0) {
            string += "Timed drawing: " + drawingSeconds + " seconds.";
            if (breakSeconds > 0) string += " ";
        }
        if (breakSeconds > 0) string += "Breaks: " + breakSeconds + " seconds.";

        return string;
    }
}
