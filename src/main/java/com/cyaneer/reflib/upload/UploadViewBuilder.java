package com.cyaneer.reflib.upload;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;

import static javafx.scene.input.TransferMode.COPY;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Builder;

public class UploadViewBuilder implements Builder<Region> {
    
    private final UploadModel model;
    private final Runnable backAction;
    private final Consumer<URI> proposeRefAction;
    private final Runnable acceptRefAction;
    private final Runnable rejectRefAction;
    BorderPane uploadView;
    
    private boolean isDragActive = false;

    public UploadViewBuilder(
        UploadModel model,
        Runnable backAction,
        Consumer<URI> proposeNewRefAction,
        Runnable acceptNewRefAction,
        Runnable rejectNewRefAction
    ){
        this.model = model;
        this.backAction = backAction;
        this.proposeRefAction = proposeNewRefAction;
        this.acceptRefAction = acceptNewRefAction;
        this.rejectRefAction = rejectNewRefAction;
    }

    @Override
    public Region build() {
        uploadView = new BorderPane();

        uploadView.setCenter(createContentRegion());
        uploadView.setBottom(createNavigationRegion());

        return uploadView;
    }

    private Region createContentRegion() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createRefUploadRegion());
        borderPane.setBottom(createSimilarRefsRegion());
        return borderPane;
    }
    
    private Region createRefUploadRegion() {
        VBox vBox = new VBox(8, createNewRefContainer(), createNewRefControls());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Region createNewRefContainer() {

        Node newRefArea = createNewRefImageView();
        newRefArea.visibleProperty().bind(model.newRefProperty().isNotNull());
        Node uploadRefArea = createUploadRefArea();
        uploadRefArea.visibleProperty().bind(model.newRefProperty().isNull());

        StackPane container = new StackPane(newRefArea, uploadRefArea);
        container.disableProperty().bind(model.isRefListLoadedProperty().not());
        container.setMinWidth(540);
        container.setMaxWidth(540);
        container.setMinHeight(540);
        container.setMaxHeight(540);
        container.setPickOnBounds(true);
        container.setStyle("-fx-border-width: 2px; -fx-border-style: segments(8); -fx-border-color: grey;");

        container.setOnDragOver(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            if (db.hasFiles() || (db.hasUrl() && db.hasImage())) {
                dragEvent.acceptTransferModes(COPY);

                if (!isDragActive) {
                    isDragActive = true;
                    container.setStyle("-fx-background-color: lightgrey; -fx-border-width: 2px; -fx-border-style: segments(8); -fx-border-color: grey;");
                }
            }
            dragEvent.consume();
        });
        container.setOnDragExited(dragEvent -> {
            if (isDragActive) {
                isDragActive = false;
                container.setStyle("-fx-background-color: transparent; -fx-border-width: 2px; -fx-border-style: segments(8); -fx-border-color: grey;");
            }
            dragEvent.consume();
        });
        container.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean isDropSuccessful = false;
            
            if (db.hasFiles() && db.getFiles().size() > 0) {
                URI uri = db.getFiles().get(0).toURI();
                proposeRefAction.accept(uri);
                isDropSuccessful = true;
            }
            else if (db.hasUrl() && db.hasImage()) {
                try {
                    URI uri = new URI(db.getUrl());
                    proposeRefAction.accept(uri);
                    isDropSuccessful = true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
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
        imageView.setFitHeight(500);
        imageView.setFitWidth(500);
        
        ObjectBinding<Image> imageBinding = createImageBinding();
        imageView.imageProperty().bind(imageBinding);

        return imageView;
    }

    private Node createUploadRefArea() {
        Label label = new Label("Drag-drop, browse or paste link to an image");

        ImageView uploadIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/cyaneer/reflib/add_photo_48.png")));

        VBox vBox = new VBox(8, label, uploadIcon, createFileChooser(), createURLInput());
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private Node createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a ref");
        fileChooser.getExtensionFilters().add(
            new ExtensionFilter("Image files", "*.png", "*.jpg")
        );
        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> {
            File selectedRef = fileChooser.showOpenDialog(null);
            proposeRefAction.accept(selectedRef.toURI());
        });

        return browseButton;
    }

    private Node createURLInput() {
        TextField textField = new TextField();
        textField.setPromptText("Paste image URL");
        textField.setPrefWidth(300);

        Button button = new Button("", new ImageView(new Image(getClass().getResourceAsStream("/com/cyaneer/reflib/download_24.png"))));
        button.setOnAction(e -> {
            try {
                proposeRefAction.accept(new URI(textField.getText()));
            } catch (URISyntaxException error) {
                //TODO: Show error in UI
            }
        });

        HBox hBox = new HBox(8, textField, button);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private ObjectBinding<Image> createImageBinding() {
        return Bindings.createObjectBinding(() -> {
            return new Image(model.getNewRef() != null ? 
                new FileInputStream(model.getNewRef().getFile()) :
                getClass().getResourceAsStream("/com/cyaneer/reflib/broken_image_48.png"));
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
        hBox.setMinHeight(250);
        hBox.setMaxHeight(250);
        hBox.setMinWidth(1300);
        hBox.setMaxWidth(1300);
        hBox.setStyle("-fx-border-width: 1px; -fx-border-color: grey;");

        model.mostSimilarRefsProperty().addListener((obs, oldRefs, newRefs) -> {
            hBox.getChildren().clear();
            newRefs.forEach(matchedRef -> {
                try {
                    ImageView refImageView = new ImageView(new Image(new FileInputStream(matchedRef.getFile())));
                    refImageView.setPreserveRatio(true);
                    refImageView.setFitHeight(200);
                    refImageView.setFitWidth(200);
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

    Region createNavigationRegion() {
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> backAction.run());

        return new HBox(backButton);
    }
}