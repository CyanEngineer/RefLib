package com.cyaneer.reflib.upload;

import java.io.File;

public class MatchedRef implements Comparable<MatchedRef> {
    private File ref;
    private int numMatches;

    public MatchedRef(File ref, int numMatches) {
        this.ref = ref;
        this.numMatches = numMatches;
    }

    public File getFile() {
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