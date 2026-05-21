package com.cyaneer.reflib.upload;

import com.cyaneer.reflib.MatchableRef;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UploadModel {
    
    private final ListProperty<MatchableRef> refList = new SimpleListProperty<MatchableRef>(FXCollections.observableArrayList());
    private final ObjectProperty<MatchableRef> newRef = new SimpleObjectProperty<MatchableRef>(null);
    private final ListProperty<MatchedRef> mostSimilarRefs = new SimpleListProperty<MatchedRef>(FXCollections.observableArrayList());
    private final IntegerProperty numSimilarRefs = new SimpleIntegerProperty(5);

    // Getters and setters for refList
    public ObservableList<MatchableRef> getRefList() {
        return refList.get();
    }

    public void setRefList(ObservableList<MatchableRef> refList) {
        this.refList.set(refList);
    }

    public ListProperty<MatchableRef> refListProperty() {
        return refList;
    }

    // Getters and setters for newRef
    public MatchableRef getNewRef() {
        return newRef.get();
    }

    public void setNewRef(MatchableRef newRef) {
        this.newRef.set(newRef);
    }

    public ObjectProperty<MatchableRef> newRefProperty() {
        return newRef;
    }

    // Getters and setters for similarRefs
    public ObservableList<MatchedRef> getMostSimilarRefs() {
        return mostSimilarRefs.get();
    }

    public void setMostSimilarRefs(ObservableList<MatchedRef> mostSimilarRefs) {
        this.mostSimilarRefs.set(mostSimilarRefs);
    }

    public ListProperty<MatchedRef> mostSimilarRefsProperty() {
        return mostSimilarRefs;
    }

    // Getters and setters for numSimilarRefs
    public int getNumSimilarRefs() {
        return numSimilarRefs.get();
    }

    public void setNumSimilarRefs(int numSimilarRefs) {
        this.numSimilarRefs.set(numSimilarRefs);
    }

    public IntegerProperty numSimilarRefsProperty() {
        return numSimilarRefs;
    }
}
