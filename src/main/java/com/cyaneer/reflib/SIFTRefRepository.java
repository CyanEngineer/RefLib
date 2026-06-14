package com.cyaneer.reflib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SIFTRefRepository implements RefRepository<MatchableRef> {

    private final File file;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SIFTRefRepository() throws IOException {
        Path appDir = Path.of(System.getProperty("user.home"), ".reflib");
        Files.createDirectories(appDir);
        file = appDir.resolve("refsFile.json").toFile();
        
        if (!file.exists()) {
            file.createNewFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, new ArrayList<SIFTRefDTO>());
        }
    }

    @Override
    public List<MatchableRef> loadRefs() throws IOException {
        List<SIFTRefDTO> dtos = objectMapper.readValue(file, new TypeReference<List<SIFTRefDTO>>() {});

        List<MatchableRef> refs = new ArrayList<>(dtos.size());
        for (SIFTRefDTO dto : dtos) {
            refs.add(dto.toSIFTMatchableRef());
        }
        return refs;
    }

    @Override
    public void saveRefs(List<MatchableRef> refs) throws IOException {
        List<SIFTRefDTO> dtos = new ArrayList<>(refs.size());
        for (MatchableRef ref : refs) {
            dtos.add(new SIFTRefDTO(ref.getFile().getAbsolutePath()));
        }
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, dtos);
    }
}
