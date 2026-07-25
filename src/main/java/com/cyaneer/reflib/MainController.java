package com.cyaneer.reflib;

import java.io.IOException;
import java.net.URI;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.practice.PracticeController;
import com.cyaneer.reflib.repository.RefRepository;
import com.cyaneer.reflib.repository.SIFTRefRepository;
import com.cyaneer.reflib.upload.UploadController;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public class MainController {
    private MainModel model;
    private MainViewBuilder viewBuilder;
    private MainInteractor interactor;

    public MainController() {
        
        model = new MainModel();

        RefRepository<MatchableRef> repository = null;
        try {
            repository = new SIFTRefRepository();
        } catch (IOException e) {
            displayError(new IOException("Failed to initialize the ref repository"));
        }

        interactor = new MainInteractor(model, repository);
        loadRefs();

        UploadController uploadController = new UploadController(
            model.refListProperty(),
            model.isRefListLoadedProperty(),
            () -> showHomePage(),
            (uri) -> {try { return createRef(uri); } catch (Exception e) {displayError(e); return null;}},
            (ref, cleanupAction) -> addRef(ref, cleanupAction)
        );

        try {
            PracticeController practiceController = new PracticeController(
                this::displayError,
                model.refListProperty(),
                model.isRefListLoadedProperty(),
                () -> showHomePage()
            );

            viewBuilder = new MainViewBuilder(
                model,
                uploadController.getView(),
                practiceController.getView()
            );
        } catch (IOException e) {
            displayError(e);
        }
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void showHomePage() {
        viewBuilder.showHomePage();
    }

    private MatchableRef createRef(URI uri) throws IOException {
        return interactor.createRef(uri);
    }

    private void addRef(MatchableRef ref, Runnable cleanupAction) {
        Task<Void> addRefTask = new Task<Void>() {
            @Override
            protected Void call() throws IOException {
                interactor.addRef(ref);
                return null;
            }
        };
        addRefTask.setOnSucceeded(event -> cleanupAction.run());
        addRefTask.setOnFailed(event -> displayError((Exception) addRefTask.getException()));

        new Thread(addRefTask).start();
    }

    private void loadRefs() {
        Task<Void> loadRefsTask = new Task<Void>() {
            @Override
            protected Void call() throws IOException {
                interactor.loadRefs();
                return null;
            }
        };
        loadRefsTask.setOnSucceeded(event -> {
            System.out.println("Refs loaded"); //TODO: Display loading status in view
            model.setIsRefListLoaded(true);
        });
        loadRefsTask.setOnFailed(event -> displayError((Exception) loadRefsTask.getException()));

        new Thread(loadRefsTask).start();
    }

    private void displayError(Exception e) { //TODO: Show stacktrace?
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("An error occurred");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
