package com.cyaneer.reflib;

import java.io.File;
import java.util.List;

public abstract class MatchableRef {

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public abstract Integer computeMatch(MatchableRef other);

    public abstract List<Integer> computeAllMatches(List<MatchableRef> others);
}
