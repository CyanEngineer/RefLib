package com.cyaneer.reflib.viewBuilder;

import java.io.FileInputStream;
import java.util.function.Consumer;

import com.cyaneer.reflib.model.UploadModel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
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
    Clipboard clipboard = Clipboard.getSystemClipboard();

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
        return new VBox(8, createNewImageContainer(), createNewRefControls());
    }

    private Region createNewImageContainer() {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(450);
        imageView.setFitWidth(800);
        
        ObjectBinding<Image> imageBinding = createImageBinding();

        imageView.imageProperty().bind(imageBinding);

        Button clipboardButton = new Button("Upload from clipboard");
        clipboardButton.setOnAction(e -> {
            if (clipboard.hasString()) {
                proposeRefAction.accept(clipboard.getString());
            } else {
                System.out.println("Clipboard does not contain a string");
            }
        });

        return new VBox(8, imageView, clipboardButton);
    }

    private ObjectBinding<Image> createImageBinding() {
        return Bindings.createObjectBinding(() -> {
            return new Image(model.getNewRef() != null ? 
                new FileInputStream(model.getNewRef().getFile()) :
                getClass().getResourceAsStream("/com/cyaneer/reflib/noimage.png"));
        }, model.newRefProperty());
    }

    private Region createNewRefControls() {
        Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(e -> acceptRefAction.run());

        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(e -> rejectRefAction.run());

        HBox controls = new HBox(8, acceptButton, rejectButton);
        controls.disableProperty().bind(model.newRefProperty().isNull());

        return controls;
    }

    private Node createSimilarRefsRegion() {
        HBox similarRefsArea = new HBox(8);
        model.mostSimilarRefsProperty().addListener((obs, oldRefs, newRefs) -> {
            similarRefsArea.getChildren().clear();
            newRefs.forEach(ref -> {
                try {
                    ImageView refImageView = new ImageView(new Image(new FileInputStream(ref.getFile())));
                    refImageView.setPreserveRatio(true);
                    refImageView.setFitHeight(100);
                    refImageView.setFitWidth(100);
                    similarRefsArea.getChildren().add(refImageView);
                } catch (Exception e) {
                    System.out.println("unable to load ref image: " + ref.getFile().getAbsolutePath());
                    e.printStackTrace();
                }
            });
        });

        return similarRefsArea;
    }
}