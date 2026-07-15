package com.cyaneer.reflib.upload;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.cyaneer.reflib.domain.MatchableRef;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;

public class UploadInteractor {
    private UploadModel model;
    private Function<URI, MatchableRef> createRefAction;
    private BiConsumer<MatchableRef, Runnable> addRefAction;

    public UploadInteractor(
        UploadModel model,
        ListProperty<MatchableRef> masterRefList,
        ObjectProperty<Boolean> isRefListLoaded,
        Function<URI, MatchableRef> createRefAction,
        BiConsumer<MatchableRef, Runnable> addRefAction
    ) {
        this.model = model;
        model.refListProperty().bind(masterRefList);
        model.isRefListLoadedProperty().bind(isRefListLoaded);
        this.createRefAction = createRefAction;
        this.addRefAction = addRefAction;
    }

    public void proposeNewRef(URI uri) {

        MatchableRef newRef = createRefAction.apply(uri);

        model.setNewRef(newRef);

        List<MatchedRef> mostSimilarRefs = findMostSimilarRefs(newRef, model.getRefList(), model.getNumSimilarRefs());
        model.setMostSimilarRefs(FXCollections.observableArrayList(mostSimilarRefs));
    }

    private List<MatchedRef> findMostSimilarRefs(MatchableRef ref, List<MatchableRef> refList, int numSimilarRefs) {
        PriorityQueue<MatchedRef> similarRefs = new PriorityQueue<>(
            numSimilarRefs,
            Comparator.comparingInt(MatchedRef::getNumMatches)
        );

        List<Integer> matchesList = ref.computeAllMatches(refList);
        for (int i = 0; i < matchesList.size(); i++) {
            if (matchesList.get(i) > 0) {
                MatchedRef candidate = new MatchedRef(refList.get(i).getFile(), matchesList.get(i));

                if (similarRefs.size() < numSimilarRefs) {
                    similarRefs.offer(candidate);
                } else if (candidate.getNumMatches() > similarRefs.peek().getNumMatches()) {
                    similarRefs.poll();
                    similarRefs.offer(candidate);
                }
            }
        }

        List<MatchedRef> mostSimilarRefs = new java.util.ArrayList<>(similarRefs);
        mostSimilarRefs.sort(Comparator.comparingInt(MatchedRef::getNumMatches).reversed());

        return mostSimilarRefs;
    }

    public void addNewRef() {
        addRefAction.accept(
            model.getNewRef(),
            () -> clearNewRef()
        );
    }

    public void clearNewRef() {
        model.setNewRef(null);
        model.setMostSimilarRefs(FXCollections.observableArrayList());
    }
}