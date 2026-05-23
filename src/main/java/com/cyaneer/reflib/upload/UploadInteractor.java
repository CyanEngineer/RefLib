package com.cyaneer.reflib.upload;

import java.util.List;
import java.util.PriorityQueue;

import com.cyaneer.reflib.MatchableRef;
import com.cyaneer.reflib.PracticeService;

import javafx.collections.FXCollections;

public class UploadInteractor {
    private UploadModel model;
    private PracticeService service = new PracticeService();

    public UploadInteractor(UploadModel model) {
        this.model = model;
    }

    public void loadRefs() {
        List<MatchableRef> refList = service.loadRefs();
        model.setRefList(FXCollections.observableArrayList(refList));
    }

    public void proposeNewRef(String filepath) {

        MatchableRef newRef = service.createNewRef(filepath);

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

    //TODO: Integrate into service
    public void acceptNewRef() {
        model.getRefList().add(model.getNewRef());
        model.setNewRef(null);
        model.setMostSimilarRefs(FXCollections.observableArrayList());
    }

    public void rejectNewRef() {
        model.setNewRef(null);
        model.setMostSimilarRefs(FXCollections.observableArrayList());
    }
}