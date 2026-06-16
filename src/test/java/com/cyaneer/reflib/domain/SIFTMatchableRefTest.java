package com.cyaneer.reflib.domain;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SIFTMatchableRefTest {
    
    @Test
    public void testComputeMatchHasHighMatchCountWhenMatchingSelf() {
        SIFTMatchableRef ref = new SIFTMatchableRef(new File("src/test/resources/testimage.png"));
        int matchCount = ref.computeMatch(ref);
        assert(matchCount > 50);
    }

    @Test
    public void testComputeMatchHasLowMatchCountWhenMatchingDifferentImage() {
        SIFTMatchableRef ref1 = new SIFTMatchableRef(new File("src/test/resources/testimage.png"));
        SIFTMatchableRef ref2 = new SIFTMatchableRef(new File("src/test/resources/testimagedifferent.jpg"));
        int matchCount = ref1.computeMatch(ref2);
        assert(matchCount < 5);
    }

    @Test
    public void testComputeMatchHasMediumMatchCountWhenMatchingRotatedImage() {
        SIFTMatchableRef ref1 = new SIFTMatchableRef(new File("src/test/resources/testimage.png"));
        SIFTMatchableRef ref2 = new SIFTMatchableRef(new File("src/test/resources/testimagerotated.png"));
        int matchCount = ref1.computeMatch(ref2);
        assert(matchCount > 30);
    }

    @Test
    public void testComputeMatchHasMediumMatchCountWhenMatchingFlippedImage() {
        SIFTMatchableRef ref1 = new SIFTMatchableRef(new File("src/test/resources/testimage.png"));
        SIFTMatchableRef ref2 = new SIFTMatchableRef(new File("src/test/resources/testimageflipped.png"));
        int matchCount = ref1.computeMatch(ref2);
        assert(matchCount > 30);
    }

    @Test
    public void testComputeMatchHasMediumMatchCountWhenMatchingEditedImage() {
        SIFTMatchableRef ref1 = new SIFTMatchableRef(new File("src/test/resources/testimage.png"));
        SIFTMatchableRef ref2 = new SIFTMatchableRef(new File("src/test/resources/testimageedited.png"));
        int matchCount = ref1.computeMatch(ref2);
        assert(matchCount > 30);
    }

    @Test
    public void testComputeAllMatchesHasHighestMatchCountForSelf() {
        SIFTMatchableRef ref1 = new SIFTMatchableRef(new File("src/test/resources/testimage.png"));
        SIFTMatchableRef ref2 = new SIFTMatchableRef(new File("src/test/resources/testimagerotated.png"));
        SIFTMatchableRef ref3 = new SIFTMatchableRef(new File("src/test/resources/testimagedifferent.jpg"));
        SIFTMatchableRef ref4 = new SIFTMatchableRef(new File("src/test/resources/testimageflipped.png"));
        SIFTMatchableRef ref5 = new SIFTMatchableRef(new File("src/test/resources/testimageedited.png"));

        List<MatchableRef> refs = Arrays.asList(ref1, ref2, ref3, ref4, ref5);
        List<Integer> matchCounts = ref1.computeAllMatches(refs);
        
        assert(matchCounts.get(0) > matchCounts.get(1));
        assert(matchCounts.get(0) > matchCounts.get(2));
        assert(matchCounts.get(0) > matchCounts.get(3));
        assert(matchCounts.get(0) > matchCounts.get(4));
    }

    @Test
    public void testComputeAllMatchesHasLowestMatchCountForDifferentImage() {
        SIFTMatchableRef ref1 = new SIFTMatchableRef(new File("src/test/resources/testimage.png"));
        SIFTMatchableRef ref2 = new SIFTMatchableRef(new File("src/test/resources/testimagerotated.png"));
        SIFTMatchableRef ref3 = new SIFTMatchableRef(new File("src/test/resources/testimagedifferent.jpg"));
        SIFTMatchableRef ref4 = new SIFTMatchableRef(new File("src/test/resources/testimageflipped.png"));
        SIFTMatchableRef ref5 = new SIFTMatchableRef(new File("src/test/resources/testimageedited.png"));

        List<MatchableRef> refs = Arrays.asList(ref1, ref2, ref3, ref4, ref5);
        List<Integer> matchCounts = ref1.computeAllMatches(refs);
        
        assert(matchCounts.get(2) < matchCounts.get(0));
        assert(matchCounts.get(2) < matchCounts.get(1));
        assert(matchCounts.get(2) < matchCounts.get(3));
        assert(matchCounts.get(2) < matchCounts.get(4));
    }
}
