package com.cyaneer.reflib.practice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.widgets.PopoutRef;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
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
        borderPane.setBottom(createNavigationRegion());
        return borderPane;
    }

    private Node createCenter() {
        GridPane gridPane = new GridPane(32, 32);

        model.currentRefProperty().addListener((ob, oldValue, newValue) -> {
            if (newValue == null) {
                // TODO: Not sure if these two if-statements might be a race condition?
                if (model.getDrawnRefsList().size() == 0) {
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

    private Node createGridNode(MatchableRef file) {
        Image image;
        try {
            image = new Image(new FileInputStream(file.getFile()));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't load image");//TODO: Decide behaviour if we fail
            image = null;
        }

        PopoutRef popoutRef = new PopoutRef(file);
        popoutRef.setMaxSize(200, 200);
        popoutRef.setMinSize(200, 200);

        VBox vBox = new VBox(8, 
            popoutRef,
            createNodeControls(image)
        );
        vBox.setMaxSize(200, 240);
        vBox.setMinSize(200, 240);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
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

    private Node createNavigationRegion() {
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
