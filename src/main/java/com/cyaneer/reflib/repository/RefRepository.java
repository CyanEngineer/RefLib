package com.cyaneer.reflib.repository;

import java.io.IOException;
import java.util.List;

import com.cyaneer.reflib.domain.Ref;

public interface RefRepository<T extends Ref> {
    
    List<T> loadRefs() throws IOException;

    void saveRefs(List<T> refs) throws IOException;
}
