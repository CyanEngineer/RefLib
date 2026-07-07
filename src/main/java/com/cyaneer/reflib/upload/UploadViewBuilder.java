package com.cyaneer.reflib.upload;

import java.io.FileInputStream;
import java.util.function.Consumer;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;

import static javafx.scene.input.TransferMode.COPY;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class UploadViewBuilder implements Builder<Region> {
    
    private final UploadModel model;
    private final Consumer<String> proposeRefAction;
    private final Runnable acceptRefAction;
    private final Runnable rejectRefAction;
    BorderPane uploadView;
    
    private boolean isDragActive = false;

    public UploadViewBuilder(
        UploadModel model,
        Consumer<String> proposeNewRefAction,
        Runnable acceptNewRefAction,
        Runnable rejectNewRefAction
    ){
        this.model = model;
        this.proposeRefAction = proposeNewRefAction;
        this.acceptRefAction = acceptNewRefAction;
        this.rejectRefAction = rejectNewRefAction;
    }

    @Override
    public Region build() {
        uploadView = new BorderPane();

        uploadView.setCenter(createRefUploadRegion());
        uploadView.setBottom(createSimilarRefsRegion());

        return uploadView;
    }
    
    private Region createRefUploadRegion() {
        VBox vBox = new VBox(8, createNewRefContainer(), createNewRefControls());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Region createNewRefContainer() {

        BorderPane container = new BorderPane(createNewRefImageView());
        container.disableProperty().bind(model.isRefListLoadedProperty().not());
        container.setMinWidth(640);
        container.setMaxWidth(640);
        container.setMinHeight(640);
        container.setMaxHeight(640);
        container.setPickOnBounds(true);
        container.setStyle("-fx-border-width: 1px; -fx-border-color: grey;");

        container.setOnDragOver(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            if (db.hasFiles() || db.hasImage()) { //TODO: Handle all relevant types
                dragEvent.acceptTransferModes(COPY);

                if (!isDragActive) {
                    isDragActive = true;
                    container.setStyle("-fx-background-color: lightgrey; -fx-border-width: 1px; -fx-border-color: grey;");
                }
            }
            dragEvent.consume();
        });
        container.setOnDragExited(dragEvent -> {
            if (isDragActive) {
                isDragActive = false;
                container.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-border-color: grey;");
            }
            dragEvent.consume();
        });
        container.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean isDropSuccessful = false;
            
            if (db.hasFiles()) {
                if (db.getFiles().size() > 0) {
                    String filePath = db.getFiles().get(0).getAbsolutePath();
                    proposeRefAction.accept(filePath);
                    isDropSuccessful = true;
                }
            }
            dragEvent.setDropCompleted(isDropSuccessful);
            dragEvent.consume();
        });

        return container;
    }

    private Node createNewRefImageView() {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(600);
        imageView.setFitWidth(600);
        
        ObjectBinding<Image> imageBinding = createImageBinding();
        imageView.imageProperty().bind(imageBinding);

        return imageView;
    }

    private ObjectBinding<Image> createImageBinding() {
        return Bindings.createObjectBinding(() -> {
            return new Image(model.getNewRef() != null ? 
                new FileInputStream(model.getNewRef().getFile()) :
                getClass().getResourceAsStream("/com/cyaneer/reflib/upload.png"));
        }, model.newRefProperty());
    }

    private Region createNewRefControls() {
        Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(e -> acceptRefAction.run());

        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(e -> rejectRefAction.run());

        HBox controls = new HBox(8, acceptButton, rejectButton);
        controls.setAlignment(Pos.CENTER);
        controls.disableProperty().bind(model.newRefProperty().isNull());

        return controls;
    }

    private Node createSimilarRefsRegion() {
        VBox vBox = new VBox(8, createSimilarRefsTitle(), createSimilarRefsContainer());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Node createSimilarRefsTitle() {
        Label title = new Label("Most similar refs (more points means more similar)");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        return title;
    }

    private Region createSimilarRefsContainer() {
        HBox hBox = new HBox(8);
        hBox.setAlignment(Pos.CENTER);
        hBox.setMinHeight(300);
        hBox.setMaxHeight(300);
        hBox.setMinWidth(1300);
        hBox.setMaxWidth(1300);
        hBox.setStyle("-fx-border-width: 1px; -fx-border-color: grey;");

        model.mostSimilarRefsProperty().addListener((obs, oldRefs, newRefs) -> {
            hBox.getChildren().clear();
            newRefs.forEach(matchedRef -> {
                try {
                    ImageView refImageView = new ImageView(new Image(new FileInputStream(matchedRef.getFile())));
                    refImageView.setPreserveRatio(true);
                    refImageView.setFitHeight(250);
                    refImageView.setFitWidth(250);
                    Label numMatchesLabel = new Label(String.valueOf(matchedRef.getNumMatches()));
                    numMatchesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                    VBox vBox = new VBox(4, refImageView, numMatchesLabel);
                    vBox.setPadding(new Insets(4));
                    vBox.setAlignment(Pos.CENTER);
                    hBox.getChildren().add(vBox);
                } catch (Exception e) {
                    System.out.println("unable to load ref image: " + matchedRef.getFile().getAbsolutePath());
                    e.printStackTrace();
                }
            });
        });

        return hBox;
    }
}