package com.cyaneer.reflib.upload;

import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

import com.cyaneer.reflib.MatchableRef;

import javafx.collections.FXCollections;

public class UploadInteractor {
    private UploadModel model;
    private Function<String, MatchableRef> createRefAction;

    public UploadInteractor(UploadModel model, Function<String, MatchableRef> createRefAction) {
        this.model = model;
        this.createRefAction = createRefAction;
    }

    public void proposeNewRef(String filepath) {

        MatchableRef newRef = createRefAction.apply(filepath);

        model.setNewRef(newRef);

        List<MatchedRef> mostSimilarRefs = findMostSimilarRefs(newRef, model.getRefList(), model.getNumSimilarRefs());
        model.setMostSimilarRefs(FXCollections.observableArrayList(mostSimilarRefs));
    }

    private List<MatchedRef> findMostSimilarRefs(MatchableRef ref, List<MatchableRef> refList, int numSimilarRefs) {
        int queueSize = refList.size() > 0 ? refList.size() : 1;
        PriorityQueue<MatchedRef> similarRefs = new PriorityQueue<>(queueSize);

        List<Integer> matchesList = ref.computeAllMatches(refList);
        for (int i = 0; i < matchesList.size(); i++) {
            similarRefs.add(new MatchedRef(refList.get(i).getFile(), matchesList.get(i)));
        }

        List<MatchedRef> mostSimilarRefs = new java.util.ArrayList<>();
        for (int i = 0; i < numSimilarRefs; i++) {
            if (similarRefs.isEmpty()) break;

            MatchedRef matchedRef = similarRefs.poll();
            if (matchedRef.getNumMatches() == 0) break;

            mostSimilarRefs.add(matchedRef);
        }

        return mostSimilarRefs;
    }

    public void clearNewRef() {
        model.setNewRef(null);
        model.setMostSimilarRefs(FXCollections.observableArrayList());
    }
}