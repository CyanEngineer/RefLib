package com.cyaneer.reflib.practice.domain;

public class SequenceStepDTO {
    private int repetitions;
    private int secPerRep;
    private SequenceStepType type;

    public static SequenceStepDTO from(SequenceStep step) {
        SequenceStepDTO dto = new SequenceStepDTO();
        dto.repetitions = step.getRepetitions();
        dto.secPerRep = step.getSecPerRep();
        dto.type = step.getType();
        return dto;
    }

    public SequenceStep toDomain() {
        return new SequenceStep(repetitions, secPerRep, type);
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getSecPerRep() {
        return secPerRep;
    }

    public void setSecPerRep(int secPerRep) {
        this.secPerRep = secPerRep;
    }

    public SequenceStepType getType() {
        return type;
    }

    public void setType(SequenceStepType type) {
        this.type = type;
    }
}
