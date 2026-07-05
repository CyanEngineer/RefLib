package com.cyaneer.reflib.widgets;

import java.io.File;

import com.cyaneer.reflib.domain.Ref;
import com.cyaneer.reflib.refViewer.RefViewerController;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopoutRef extends Region {

    private Ref ref;
    private Runnable onClickAction = () -> {};
    private BorderPane container;
    
    public PopoutRef(Ref ref) {
        this.ref = ref;
        createLayout();
    }

    public PopoutRef(Ref ref, Runnable onClickAction) {
        this.ref = ref;
        this.onClickAction = onClickAction;
        createLayout();
    }

    private void createLayout() {
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(ref.getFile().toURI().toString()));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setOnMouseClicked(e -> onClickAction.run());
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());
        
        Button button = new Button("", new ImageView(new File("src/main/resources/com/cyaneer/reflib/open_in_new.png").toURI().toString()));
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");
        button.setOnAction(e -> popOut());

        StackPane stackPane = new StackPane(imageView, button);
        StackPane.setAlignment(button, Pos.BOTTOM_RIGHT);
        stackPane.setMaxWidth(Region.USE_PREF_SIZE);
        stackPane.setMaxHeight(Region.USE_PREF_SIZE);

        container = new BorderPane();
        container.setCenter(stackPane);
        getChildren().setAll(container);
    }

    @Override
    protected void layoutChildren() {
        container.resizeRelocate(0, 0, getWidth(), getHeight());
    }

    private void popOut() {
        Stage window = new Stage();
        window.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(new RefViewerController(ref).getView(), 400, 400, Color.TRANSPARENT);
        scene.getRoot().setStyle("-fx-background-color: transparent");
        window.setScene(scene);

        window.show();
    }
}
