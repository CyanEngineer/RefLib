package com.cyaneer.reflib.viewBuilder;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HomeViewBuilder implements Builder<Region>{
    BorderPane homeView;
    Region homePage;
    Region uploadPage;
    Region practicePage;

    public HomeViewBuilder(Region uploadPage, Region practicePage) {
        this.uploadPage = uploadPage;
        this.practicePage = practicePage;
    }
    
    @Override
    public Region build() {
        homePage = createHomePage();

        homeView = new BorderPane();
        homeView.setPadding(new Insets(8));
        homeView.setCenter(homePage);
        return homeView;
    }

    private Region createHomePage() {
        Button uploadPageButton = new Button("Upload");
        uploadPageButton.setOnAction(e -> homeView.setCenter(uploadPage));

        Button practicePageButton = new Button("Practice");
        practicePageButton.setOnAction(e -> homeView.setCenter(practicePage));

        return new HBox(
            8,
            uploadPageButton,
            practicePageButton
        );
    }
}
