package com.cyaneer.reflib;

import java.io.IOException;
import java.util.List;

public interface RefRepository<T extends Ref> {
    
    List<T> loadRefs() throws IOException;

    void saveRefs(List<T> refs) throws IOException;
}
