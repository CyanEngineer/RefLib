package com.cyaneer.reflib.viewBuilder;

import com.cyaneer.reflib.model.SequenceStep;
import com.cyaneer.reflib.model.SequenceStepType;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

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
        Label label = new Label();
        label.textProperty().bind(Bindings.createStringBinding(
            () -> model.getRepetitions() + " poses of " + model.getDuration().toSeconds() + " sec",
            model.repetitionsProperty(), model.durationProperty())
        );
        return label;
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
            () -> model.getDuration().toSeconds() + " seconds break",
            model.durationProperty())
        );
        return label;
    }
    
    @Override
    public void updateItem(SequenceStep item, boolean isEmpty) {
        model.unbind();
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
