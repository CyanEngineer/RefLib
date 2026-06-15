package com.cyaneer.reflib.domain;

import java.util.List;

public abstract class MatchableRef extends Ref {

    public abstract Integer computeMatch(MatchableRef other);

    public abstract List<Integer> computeAllMatches(List<MatchableRef> others);
}
