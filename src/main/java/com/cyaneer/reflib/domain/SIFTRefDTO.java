package com.cyaneer.reflib.domain;

import java.io.File;

public class SIFTRefDTO {
    private String filePath;

    public SIFTRefDTO() {
        
    }
    
    public SIFTRefDTO(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public SIFTMatchableRef toSIFTMatchableRef() {
        return new SIFTMatchableRef(new File(filePath));
    }
}
