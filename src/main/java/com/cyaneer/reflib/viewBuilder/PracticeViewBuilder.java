package com.cyaneer.reflib.viewBuilder;

import java.util.function.Consumer;

import com.cyaneer.reflib.model.PracticeModel;
import com.cyaneer.reflib.model.SequenceStepType;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class PracticeViewBuilder implements Builder<Region>{

    private final PracticeModel model;
    private final Consumer<SequenceStepType> addStepAction;
    private final Consumer<Integer> removeStepAction;
    private final Runnable startPracticeAction;
    private final Runnable startTimerAction;
    private final Runnable pauseTimerAction;
    private final Runnable stopTimerAction;
    private final Runnable jumpToNextAction;
    private final Runnable resetPracticeAction;
    BorderPane practiceView;
    Region practicePlanView;
    Region practiceSessionView;
    Region practiceReviewView;

    public PracticeViewBuilder(
        PracticeModel model,
        Consumer<SequenceStepType> addStepAction,
        Consumer<Integer> removeStepAction,
        Runnable startPracticeAction,
        Runnable startTimerAction,
        Runnable pauseTimerAction,
        Runnable stopTimerAction,
        Runnable jumpToNextAction,
        Runnable resetPracticeAction
    ) {
        this.model = model;
        this.addStepAction = addStepAction;
        this.removeStepAction = removeStepAction;
        this.startPracticeAction = startPracticeAction;
        this.startTimerAction = startTimerAction;
        this.pauseTimerAction = pauseTimerAction;
        this.stopTimerAction = stopTimerAction;
        this.jumpToNextAction = jumpToNextAction;
        this.resetPracticeAction = resetPracticeAction;
    }

    @Override
    public Region build() {
        practiceView = new BorderPane();

        practicePlanView = new PracticePlanViewBuilder(
            model,
            addStepAction,
            removeStepAction,
            planViewStartAction()
        ).build();

        practiceSessionView = new PracticeSessionViewBuilder(
            model,
            sessionViewBackAction(),
            sessionViewReviewAction(),
            startTimerAction,
            pauseTimerAction,
            jumpToNextAction
        ).build();

        practiceReviewView = new practiceReviewViewBuilder(
            model,
            reviewViewBackAction()
        ).build();
        
        practiceView.setCenter(practicePlanView);

        List<String> runtimeArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        boolean isDebugMode = runtimeArguments.stream().anyMatch(arg -> arg.contains("jdwp"));
        if (isDebugMode) {
            practiceView.setBottom(createDebugButton());
        }
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
