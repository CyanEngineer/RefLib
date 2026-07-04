package com.cyaneer.reflib;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.domain.SIFTMatchableRef;
import com.cyaneer.reflib.repository.RefRepository;

import javafx.collections.FXCollections;

public class MainInteractor {
    private final MainModel model;
    private final RefRepository<MatchableRef> repository;

    public MainInteractor(MainModel model, RefRepository<MatchableRef> repository) {
        this.model = model;
        this.repository = repository;
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
