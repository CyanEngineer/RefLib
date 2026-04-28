package com.cyaneer.reflib;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.cyaneer.reflib.model.Ref;
import com.cyaneer.reflib.model.SequenceStep;
import com.cyaneer.reflib.model.SequenceStepType;

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

    public List<Ref> loadRefs() { //TODO: Load images from a config file
        return Arrays.asList();
    }

    public List<SequenceStep> loadSequence() { // TODO: Use a proper config file and maybe a DTO
        return Arrays.asList(
            new SequenceStep(15, 60, SequenceStepType.TIMED_POSES),
            new SequenceStep(1, 60, SequenceStepType.BREAK),
            new SequenceStep(15, 60, SequenceStepType.TIMED_POSES)
        );
    }

    public Ref createNewRef(String filepath) {
        //TODO: Allow internet images
        File file = new File(filepath);

        return new Ref(file);
    }
}
