package com.cyaneer.reflib;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PracticeService {
    
    public List<File> loadImages() {
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
}
