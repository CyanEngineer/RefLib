package com.cyaneer.reflib;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.domain.SIFTMatchableRef;
import com.cyaneer.reflib.practice.domain.Sequence;
import com.cyaneer.reflib.practice.domain.SequenceStep;
import com.cyaneer.reflib.practice.domain.SequenceStepType;

public class PracticeService {
    
    public List<File> loadImages() { // TODO: Use a proper config file
        try {
            Scanner sc = new Scanner(new File("imagesPath.txt"));
            String imagePath = sc.nextLine();
            sc.close();

            File dir = new File(imagePath);
            File[] dirListing = dir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isFile();
                }
            });
            return Arrays.asList(dirListing);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MatchableRef> loadRefs() { //TODO: Load images from a config file
        return Arrays.asList();
    }

    public List<Sequence> loadSequences() { // TODO: Use a proper config file and maybe a DTO
        Sequence standard = new Sequence();
        standard.setName("Standard");
        standard.addStep(new SequenceStep(15, 60, SequenceStepType.TIMED_REFS));
        standard.addStep(new SequenceStep(1, 60, SequenceStepType.BREAK));
        standard.addStep(new SequenceStep(15, 60, SequenceStepType.TIMED_REFS));

        Sequence test = new Sequence();
        test.setName("test");
        test.addStep(new SequenceStep(5, 5, SequenceStepType.TIMED_REFS));
        test.addStep(new SequenceStep(2, 0, SequenceStepType.UNTIMED_REFS));
        test.addStep(new SequenceStep(1, 10, SequenceStepType.BREAK));
        
        return Arrays.asList(standard, test);
    }

    public SIFTMatchableRef createNewRef(String filepath) {
        //TODO: Allow internet images
        File file = new File(filepath);

        return new SIFTMatchableRef(file);
    }
}
