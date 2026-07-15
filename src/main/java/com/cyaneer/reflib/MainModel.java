package com.cyaneer.reflib;

import java.io.File;

import com.cyaneer.reflib.domain.MatchableRef;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel {
    private final ListProperty<MatchableRef> refList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final ObjectProperty<Boolean> isRefListLoaded = new SimpleObjectProperty<Boolean>(false);
    private final ObjectProperty<File> tmpFile = new SimpleObjectProperty<File>(null);

    public ObservableList<MatchableRef> getRefList() {
        return refList.get();
    }

    public void setRefList(ObservableList<MatchableRef> refList) {
        this.refList.set(refList);
    }

    public ListProperty<MatchableRef> refListProperty() {
        return refList;
    }

    public boolean isRefListLoaded() {
        return isRefListLoaded.get();
    }

    public void isRefListLoaded(boolean isLoadingComplete) {
        this.isRefListLoaded.set(isLoadingComplete);
    }

    public void setIsRefListLoaded(boolean isLoadingComplete) {
        this.isRefListLoaded.set(isLoadingComplete);
    }

    public ObjectProperty<Boolean> isRefListLoadedProperty() {
        return isRefListLoaded;
    }

    public File getTmpFile() {
        return tmpFile.get();
    }

    public void setTmpFile(File tmpFile) {
        this.tmpFile.set(tmpFile);
    }

    public ObjectProperty<File> tmpFileProperty() {
        return tmpFile;
    }
}
