package com.cyaneer.reflib.refViewer;

import java.io.File;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

public class RefViewerViewBuilder implements Builder<Region> {

    private RefViewerModel model;

    public RefViewerViewBuilder(RefViewerModel model) {
        this.model = model;
    }
    
    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        
        borderPane.setCenter(createImageArea());
        borderPane.setBottom(createControlsArea());

        return borderPane;
    }

    private Node createImageArea() {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        StackPane center = new StackPane(imageView);
        center.setMinSize(0, 0);
        center.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        imageView.fitHeightProperty().bind(center.heightProperty());
        imageView.fitWidthProperty().bind(center.widthProperty());

        imageView.opacityProperty().bind(model.imageOpacityProperty());
        imageView.rotateProperty().bind(model.imageRotationProperty());
        
        imageView.imageProperty().bind(createImageBinding());
        
        return center;
    }

    private ObjectBinding<Image> createImageBinding() {
        return Bindings.createObjectBinding(() -> {
            return new Image(model.getRef() != null ? 
                model.getRef().getFile().toURI().toString() :
                new File("src/main/resources/com/cyaneer/reflib/noimage.png").toURI().toString());
        }, model.refProperty());
    }

    private Node createControlsArea() {
        Slider opacitySlider = new Slider(0, 1, 1);
        opacitySlider.valueProperty().bindBidirectional(model.imageOpacityProperty());

        Slider rotationSlider = new Slider(-180, 180, 0);
        rotationSlider.valueProperty().bindBidirectional(model.imageRotationProperty());

        return new HBox(8, new Label("Opacity:"), opacitySlider, new Label("Rotation:"), rotationSlider);
    }
}