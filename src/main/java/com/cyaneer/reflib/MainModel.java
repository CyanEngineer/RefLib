package com.cyaneer.reflib;

import com.cyaneer.reflib.domain.MatchableRef;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel {
    private final ListProperty<MatchableRef> refList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final BooleanProperty isRefListLoaded = new SimpleBooleanProperty(false);

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

    public BooleanProperty isRefListLoadedProperty() {
        return isRefListLoaded;
    }
}
