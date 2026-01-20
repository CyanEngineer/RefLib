package com.cyaneer.reflib.viewBuilder;

import com.cyaneer.reflib.model.PracticeModel;
import com.cyaneer.reflib.model.SequenceStep;

import javafx.beans.binding.Bindings;
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
    private final Runnable startAction;
    
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
        listView.setItems(model.getSequenceStepList());
        System.out.println(listView.getItems().getFirst().getType().name());
        return listView;
    }

    private SequenceStepCell createCell() {
        return new SequenceStepCell();
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
}
