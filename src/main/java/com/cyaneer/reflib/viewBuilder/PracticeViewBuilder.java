package com.cyaneer.reflib.viewBuilder;

import com.cyaneer.reflib.PracticeModel;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PracticeViewBuilder implements Builder<Region>{

    private final PracticeModel model;
    private final Runnable startTimerAction;
    private final Runnable pauseTimerAction;
    private final Runnable stopTimerAction;
    Region practicePlanView;
    Region practiceSessionView;

    public PracticeViewBuilder(PracticeModel model, Runnable startTimerAction, Runnable pauseTimerAction, Runnable stopTimerAction) {
        this.model = model;
        this.startTimerAction = startTimerAction;
        this.pauseTimerAction = pauseTimerAction;
        this.stopTimerAction = stopTimerAction;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        practicePlanView = new PracticePlanViewBuilder(model, () -> {
            borderPane.setCenter(practiceSessionView);
            startTimerAction.run();
        }).build();
        practiceSessionView = new PracticeSessionViewBuilder(
            model,
            () -> borderPane.setCenter(practicePlanView),
            startTimerAction,
            pauseTimerAction,
            stopTimerAction
        ).build();
        borderPane.setCenter(practicePlanView);
        return borderPane;
    }
    
}
