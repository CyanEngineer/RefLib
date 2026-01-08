package com.cyaneer.reflib.viewBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cyaneer.reflib.model.PracticeModel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class practiceReviewViewBuilder implements Builder<Region> {

    private final PracticeModel model;
    private final Runnable backButtonAction;
    private final int columnCount = 5;
    private int gridNodeCount = 0;
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final ClipboardContent clipboardContent = new ClipboardContent();

    public practiceReviewViewBuilder(PracticeModel model, Runnable backAction) {
        this.model = model;
        this.backButtonAction = backAction;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createCenter());
        borderPane.setRight(createRight());
        borderPane.setBottom(createBottom());
        return borderPane;
    }

    private Node createCenter() {
        GridPane gridPane = new GridPane(32, 32);

        model.currentPoseProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue == null) {
                // TODO: Not sure if these two if-statements might be a race condition?
                if (model.getDrawnPosesList().size() == 0) {
                    System.out.println("Get cleared!");
                    gridPane.getChildren().clear();
                    gridNodeCount = 0;
                }
            }
            else {
                Node node = createGridNode(newValue);
                gridPane.add(node, gridNodeCount % columnCount, gridNodeCount / columnCount);
                gridNodeCount++;
            }
        });

        return new ScrollPane(gridPane);
    }

    private Node createGridNode(File file) {
        Image image;
        try {
            image = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't load image");//TODO: Decide behaviour if we fail
            image = null;
        }

        VBox vBox = new VBox(8, 
            createNodeImage(image),
            createNodeControls(image)
        );
        vBox.setPrefSize(200, 240);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Node createNodeImage(Image image) {
        int imageSize = 200;
        ImageView imageView = new ImageView();
        imageView.setFitHeight(imageSize);
        imageView.setFitWidth(imageSize);
        imageView.setPreserveRatio(true);
        imageView.setImage(image);

        HBox hBox = new HBox(imageView);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefSize(imageSize, imageSize);
        
        return hBox;
    }

    private Node createNodeControls(Image image) {
        Button button = new Button("Copy");
        button.setOnAction(e -> {
            clipboardContent.putImage(image);
            clipboard.setContent(clipboardContent);
        });
        return button;
    }

    private Node createRight() {
        return new Label("Put image data here");
    }

    private Node createBottom() {
        Button button = new Button("Back");
        button.setOnAction(e -> {
            backButtonAction.run();
        });
        HBox hBox = new HBox(8,
            button
        );
        return hBox;
    }
    
}
