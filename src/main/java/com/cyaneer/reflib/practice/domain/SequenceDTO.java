package com.cyaneer.reflib.practice.domain;

import java.util.List;

public class SequenceDTO {
    private String name;
    private List<SequenceStepDTO> steps;

    public static SequenceDTO from(Sequence sequence) {
        SequenceDTO dto = new SequenceDTO();
        dto.name = sequence.getName();
        dto.steps = sequence.getSteps().stream().map(step -> SequenceStepDTO.from(step)).toList();

        return dto;
    }

    public Sequence toDomain() {
        Sequence sequence = new Sequence();
        sequence.setName(name);
        sequence.setSteps(steps.stream().map(step -> step.toDomain()).toList());

        return sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SequenceStepDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<SequenceStepDTO> sequenceList) {
        this.steps = sequenceList;
    }
}
