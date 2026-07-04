package com.cyaneer.reflib.practice.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.cyaneer.reflib.practice.domain.Sequence;
import com.cyaneer.reflib.practice.domain.SequenceDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONSequenceRepository implements SequenceRepository {
    private final File file;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public JSONSequenceRepository() throws IOException {
        Path appDir = Path.of(System.getProperty("user.home"), ".reflib");
        Files.createDirectories(appDir);
        file = appDir.resolve("sequencesFile.json").toFile();

        if (!file.exists()) {
            file.createNewFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, new ArrayList<>());
        }
    }

    @Override
    public List<Sequence> loadSequences() throws IOException {
        List<SequenceDTO> dtos = objectMapper.readValue(file, new TypeReference<List<SequenceDTO>>() {});

        List<Sequence> sequences = new ArrayList<Sequence>(dtos.size());
        for (SequenceDTO dto : dtos) {
            sequences.add(dto.toDomain());
        }

        return sequences;
    }

    @Override
    public void saveSequences(List<Sequence> sequences) throws IOException {
        List<SequenceDTO> dtos = new ArrayList<>(sequences.size());
        for (Sequence sequence : sequences) {
            dtos.add(SequenceDTO.from(sequence));
        }
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, dtos);
    }
}
