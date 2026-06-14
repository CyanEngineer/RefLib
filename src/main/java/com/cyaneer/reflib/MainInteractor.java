package com.cyaneer.reflib;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;

public class MainInteractor {
    private final MainModel model;
    private SIFTRefRepository repository;

    public MainInteractor(MainModel model) throws IOException {
        this.model = model;
        repository = new SIFTRefRepository();
    }

    public MatchableRef createRef(String filepath) {
        return new SIFTMatchableRef(new File(filepath));
    }

    public void addRef(MatchableRef ref) throws IOException {
        model.getRefList().add(ref);
        saveRefs();
    }

    public void loadRefs() throws IOException {
        List<MatchableRef> refList = repository.loadRefs();
        model.setRefList(FXCollections.observableArrayList(refList));
    }

    public void saveRefs() throws IOException {
        repository.saveRefs(model.getRefList());
    }
}
