package com.cyaneer.reflib;

import java.io.IOException;
import java.net.URI;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.practice.PracticeController;
import com.cyaneer.reflib.repository.RefRepository;
import com.cyaneer.reflib.repository.SIFTRefRepository;
import com.cyaneer.reflib.upload.UploadController;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;

public class MainController {
    private MainModel model;
    private MainViewBuilder viewBuilder;
    private MainInteractor interactor;

    public MainController() {
        
        model = new MainModel();

        RefRepository<MatchableRef> repository = null;
        try { // TODO: Handle
            repository = new SIFTRefRepository();
        } catch (IOException e) {
            e.printStackTrace();
        }

        interactor = new MainInteractor(model, repository);
        loadRefs();

        UploadController uploadController = new UploadController(
            model.refListProperty(),
            model.isRefListLoadedProperty(),
            () -> showHomePage(),
            (uri) -> createRef(uri),
            (ref, cleanupAction) -> addRef(ref, cleanupAction)
        );

        PracticeController practiceController = new PracticeController(
            model.refListProperty(),
            model.isRefListLoadedProperty(),
            () -> showHomePage()
        );

        viewBuilder = new MainViewBuilder(
            model,
            uploadController.getView(),
            practiceController.getView()
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void showHomePage() {
        viewBuilder.showHomePage();
    }

    private MatchableRef createRef(URI uri) {
        return interactor.createRef(uri);
    }

    private void addRef(MatchableRef ref, Runnable cleanupAction) {
        Task<Void> addRefTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    interactor.addRef(ref);
                } catch (IOException e) {
                    e.printStackTrace(); //TODO: Proper error handling
                }
                return null;
            }
        };
        addRefTask.setOnSucceeded(event -> cleanupAction.run());

        new Thread(addRefTask).start();
    }

    private void loadRefs() {
        Task<Void> loadRefsTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    interactor.loadRefs();
                } catch (IOException e) {
                    e.printStackTrace(); //TODO: Proper error handling
                }
                return null;
            }
        };
        loadRefsTask.setOnSucceeded(event -> {
            System.out.println("Refs loaded"); //TODO: Display loading status in view
            model.setIsRefListLoaded(true);
        });

        new Thread(loadRefsTask).start();
    }
}
