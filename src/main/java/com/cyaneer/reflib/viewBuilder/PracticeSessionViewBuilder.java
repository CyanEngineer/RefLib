package com.cyaneer.reflib.viewBuilder;

import java.io.FileInputStream;

import com.cyaneer.reflib.model.PracticeModel;
import com.cyaneer.reflib.model.SequenceStepType;

import javafx.animation.Animation.Status;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

public class PracticeSessionViewBuilder implements Builder<Region>{

    private final PracticeModel model;
    private final Runnable backButtonAction;
    private final Runnable reviewButtonAction;
    private final Runnable startTimerAction;
    private final Runnable pauseTimerAction;
    private final Runnable jumpToNextAction;

    public PracticeSessionViewBuilder(
        PracticeModel model,
        Runnable backAction,
        Runnable reviewAction,
        Runnable startTimerAction,
        Runnable pauseTimerAction,
        Runnable jumpToNextAction
    ) {
        this.model = model;
        this.backButtonAction = backAction;
        this.reviewButtonAction = reviewAction;
        this.startTimerAction = startTimerAction;
        this.pauseTimerAction = pauseTimerAction;
        this.jumpToNextAction = jumpToNextAction;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createCenter());
        borderPane.setBottom(createBottom());
        return borderPane;
    }

    private Node createCenter() {
        Node imageContainer = createImageContainer();
        imageContainer.visibleProperty().bind(model.currentSequenceStepTypeProperty().isNotEqualTo(SequenceStepType.BREAK));
        Node breakElement = createBreakElement();
        breakElement.visibleProperty().bind(model.currentSequenceStepTypeProperty().isEqualTo(SequenceStepType.BREAK));
        return new StackPane(imageContainer, breakElement);
    }
    
    private Node createImageContainer() {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(900);
        imageView.setFitWidth(1600);
        
        ObjectBinding<Image> imageBinding = createImageBinding();

        imageView.imageProperty().bind(imageBinding);
        return imageView;
    }

    private ObjectBinding<Image> createImageBinding() {
        return Bindings.createObjectBinding(() -> {
            return new Image(model.getcurrentPose() != null ? 
                new FileInputStream(model.getcurrentPose()) :
                getClass().getResourceAsStream("/com/cyaneer/reflib/noimage.png"));
        }, model.currentPoseProperty());
    }

    private Node createBreakElement() {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(900);
        imageView.setFitWidth(1600);

        imageView.setImage(new Image(getClass().getResourceAsStream("/com/cyaneer/reflib/break.png")));
        return imageView;
    }

    private Node createBottom() {
        
        HBox hBox = new HBox(64,
            createActionButton("Back", backButtonAction),
            createProgressElement(),
            createControls()
        );
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private Node createActionButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    private Node createStepProgressElement() {
        Label label = new Label("");
        StringBinding stringBinding = Bindings.createStringBinding(() -> {
            return model.getCurrentPoseNumber() + "/" + model.getCurrentSequenceStepRepetitions();
        }, model.currentPoseNumberProperty(), model.currentSequenceStepRepetitionsProperty());
        label.textProperty().bind(stringBinding);
        return label;
    }

    private Node createProgressElement() {
        return new HBox(8, 
            createStepProgressElement(),
            createPoseProgressElement()
        );
    }

    private Node createPoseProgressElement() {
        Label untimedLabel = new Label("Untimed poses");
        untimedLabel.visibleProperty().bind(model.currentSequenceStepTypeProperty().isEqualTo(SequenceStepType.UNTIMED_POSES));
        Node timedProgressBar = createTimedProgressBar();
        timedProgressBar.visibleProperty().bind(model.currentSequenceStepTypeProperty().isNotEqualTo(SequenceStepType.UNTIMED_POSES));
        return new StackPane(untimedLabel, timedProgressBar);
    }

    private Node createTimedProgressBar() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);
        DoubleBinding progress = Bindings.createDoubleBinding(() -> {
            double elapsedSeconds = Math.floor(model.getElapsedSeconds());
            double totalSeconds = model.getCurrentSequenceStepSecPerRep();
            return elapsedSeconds / (totalSeconds - 1);
        }, model.currentElapsedSecondsProperty());
        progressBar.progressProperty().bind(progress);
        return progressBar;
    }
    
    private Node createControls() {

        Node progressControls = createProgressControls();
        progressControls.visibleProperty().bind(model.isSessionFinishedProperty().not());
        progressControls.managedProperty().bind(progressControls.visibleProperty());

        Button reviewButton = new Button("Go to review");
        reviewButton.visibleProperty().bind(model.isSessionFinishedProperty());
        reviewButton.managedProperty().bind(reviewButton.visibleProperty());
        reviewButton.setOnAction(e -> reviewButtonAction.run());

        return new StackPane(progressControls, reviewButton);
    }

    private Node createProgressControls() {

        Node timerControls = createTimerControls();
        timerControls.visibleProperty().bind(model.currentSequenceStepTypeProperty().isNotEqualTo(SequenceStepType.UNTIMED_POSES));

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> jumpToNextAction.run());

        return new HBox(8, timerControls, nextButton);
    }

    private Node createTimerControls() {

        Button startButton = new Button("Start");
        startButton.visibleProperty().bind(model.timerStatusProperty().isNotEqualTo(Status.RUNNING));
        startButton.managedProperty().bind(startButton.visibleProperty());
        startButton.setOnAction(e -> startTimerAction.run());

        Button pauseButton = new Button("Pause");
        pauseButton.visibleProperty().bind(model.timerStatusProperty().isEqualTo(Status.RUNNING));
        pauseButton.managedProperty().bind(pauseButton.visibleProperty());
        pauseButton.setOnAction(e -> pauseTimerAction.run());

        return new StackPane(startButton, pauseButton);
    }
}
