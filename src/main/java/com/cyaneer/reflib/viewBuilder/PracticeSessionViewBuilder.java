package com.cyaneer.reflib.viewBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cyaneer.reflib.model.PracticeModel;

import javafx.animation.Animation.Status;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
    private final Runnable stopTimerAction;

    public PracticeSessionViewBuilder(
        PracticeModel model,
        Runnable backAction,
        Runnable reviewAction,
        Runnable startTimerAction,
        Runnable pauseTimerAction,
        Runnable stopTimerAction
    ) {
        this.model = model;
        this.backButtonAction = backAction;
        this.reviewButtonAction = reviewAction;
        this.startTimerAction = startTimerAction;
        this.pauseTimerAction = pauseTimerAction;
        this.stopTimerAction = stopTimerAction;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createCenter());
        borderPane.setBottom(createBottom());
        return borderPane;
    }

    private Node createCenter() {
        return createImageContainer();
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
            try {
                File file = model.getcurrentPose() != null ? 
                    model.getcurrentPose() : 
                    new File("src/main/resources/com/cyaneer/reflib/noimage.png");
                return new Image(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't load image");
                return null; //TODO: Decide behaviour if we fail
            }
        }, model.currentPoseProperty());
    }

    private Node createBottom() {
        
        HBox hBox = new HBox(64,
            createActionButton("Back", backButtonAction),
            createProgressElement(),
            createTimerControls()
        );
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private Node createActionButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    private Node createConditionalActionButton(String text, Runnable action, BooleanBinding disablingBinding) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        button.disableProperty().bind(disablingBinding);
        return button;
    }

    private Node createSessionProgressElement() {
        Label label = new Label("");
        StringBinding stringBinding = Bindings.createStringBinding(() -> {
            return model.getCurrentPoseNumber() + "/" + model.getNumberOfPoses();
        }, model.currentPoseNumberProperty(), model.numberOfPosesProperty());
        label.textProperty().bind(stringBinding);
        return label;
    }

    private Node createProgressElement() {
        return new HBox(8, 
            createSessionProgressElement(),
            createPoseProgressElement()
        );
    }

    private Node createPoseProgressElement() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);
        DoubleBinding progress = Bindings.createDoubleBinding(() -> {
            double elapsedSeconds = Math.floor(model.getElapsedSeconds().toSeconds());
            return elapsedSeconds / (model.getSecondsPerPose() - 1);
        }, model.currentElapsedSecondsProperty());
        progressBar.progressProperty().bind(progress);
        return progressBar;
    }
    
    private Node createTimerControls() {

        Button startButton = new Button("Start");
        startButton.visibleProperty().bind(
            model.timerStatusProperty().isNotEqualTo(Status.RUNNING)
            .and(model.isSessionFinishedProperty().not())
        );
        startButton.managedProperty().bind(startButton.visibleProperty());
        startButton.setOnAction(e -> startTimerAction.run());

        Button pauseButton = new Button("Pause");
        pauseButton.visibleProperty().bind(
            model.timerStatusProperty().isEqualTo(Status.RUNNING)
            .and(model.isSessionFinishedProperty().not())
        );
        pauseButton.managedProperty().bind(pauseButton.visibleProperty());
        pauseButton.setOnAction(e -> pauseTimerAction.run());

        Button reviewButton = new Button("Go to review");
        reviewButton.visibleProperty().bind(model.isSessionFinishedProperty());
        reviewButton.managedProperty().bind(reviewButton.visibleProperty());
        reviewButton.setOnAction(e -> reviewButtonAction.run());

        return new StackPane(startButton, pauseButton, reviewButton);
    }
}
