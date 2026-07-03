package com.cyaneer.reflib.practice.repository;

import java.io.IOException;
import java.util.List;

import com.cyaneer.reflib.practice.domain.Sequence;

public interface SequenceRepository {

    public List<Sequence> loadSequences() throws IOException;

    public void saveSequences(List<Sequence> sequences) throws IOException;
}
