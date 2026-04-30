package com.cyaneer.reflib.model;



public class MatchedRef implements Comparable<MatchedRef> {
    private Ref ref;
    private int numMatches;

    public MatchedRef(Ref ref, int numMatches) {
        this.ref = ref;
        this.numMatches = numMatches;
    }

    public Ref getRef() {
        return ref;
    }

    public int getNumMatches() {
        return numMatches;
    }

    @Override
    public int compareTo(MatchedRef other) {
        return Integer.compare(other.numMatches, this.numMatches); // Sort in descending order
    }
}