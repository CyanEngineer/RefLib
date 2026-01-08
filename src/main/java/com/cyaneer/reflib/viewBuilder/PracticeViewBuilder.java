package com.cyaneer.reflib.viewBuilder;

import com.cyaneer.reflib.model.PracticeModel;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PracticeViewBuilder implements Builder<Region>{

    private final PracticeModel model;
    private final Runnable startPracticeAction;
    private final Runnable startTimerAction;
    private final Runnable pauseTimerAction;
    private final Runnable stopTimerAction;
    private final Runnable resetPracticeAction;
    BorderPane practiceView;
    Region practicePlanView;
    Region practiceSessionView;
    Region practiceReviewView;

    public PracticeViewBuilder(
        PracticeModel model,
        Runnable startPracticeAction,
        Runnable startTimerAction,
        Runnable pauseTimerAction,
        Runnable stopTimerAction,
        Runnable resetPracticeAction
    ) {
        this.model = model;
        this.startPracticeAction = startPracticeAction;
        this.startTimerAction = startTimerAction;
        this.pauseTimerAction = pauseTimerAction;
        this.stopTimerAction = stopTimerAction;
        this.resetPracticeAction = resetPracticeAction;
    }

    @Override
    public Region build() {
        practiceView = new BorderPane();

        practicePlanView = new PracticePlanViewBuilder(
            model, 
            planViewStartAction()
        ).build();

        practiceSessionView = new PracticeSessionViewBuilder(
            model,
            sessionViewBackAction(),
            sessionViewReviewAction(),
            startTimerAction,
            pauseTimerAction,
            stopTimerAction
        ).build();

        practiceReviewView = new practiceReviewViewBuilder(
            model,
            reviewViewBackAction()
        ).build();

        
        
        practiceView.setCenter(practicePlanView);
        practiceView.setBottom(createDebugButton());
        return practiceView;
    }
    
    private Runnable planViewStartAction() {
        return () -> {
            practiceView.setCenter(practiceSessionView);
            startPracticeAction.run();
        };
    }

    private Runnable sessionViewBackAction() {
        return () -> {
            practiceView.setCenter(practicePlanView);
            stopTimerAction.run();
            resetPracticeAction.run();
        };
    }

    private Runnable sessionViewReviewAction() {
        return () -> {
            practiceView.setCenter(practiceReviewView);
            stopTimerAction.run();
        };
    }

    private Runnable reviewViewBackAction() {
        return () -> {
            practiceView.setCenter(practicePlanView);
            resetPracticeAction.run();
        };
    }

    private Node createDebugButton() {
        Button button = new Button("Enter debug");
        button.setOnAction(e -> model.enterDebug());
        return button;
    }
}
