package com.cyaneer.reflib;

import java.util.List;

public interface RefRepository {
    
    List<Ref> loadRefs();

    void saveRefs(List<Ref> refs);
}
