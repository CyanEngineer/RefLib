package com.cyaneer.reflib;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    
    public static void main(String[] args) {
        //System.setProperty("org.bytedeco.javacpp.logger.debug", "true");
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        //Scene scene = new Scene(new PracticeController().getView(), 1600, 900);
        Scene scene = new Scene(new UploadController().getView(), 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}