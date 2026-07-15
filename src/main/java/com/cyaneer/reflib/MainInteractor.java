package com.cyaneer.reflib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cyaneer.reflib.domain.MatchableRef;
import com.cyaneer.reflib.domain.SIFTMatchableRef;
import com.cyaneer.reflib.repository.RefRepository;

import javafx.collections.FXCollections;

public class MainInteractor {
    private final MainModel model;
    private final RefRepository<MatchableRef> repository;

    public MainInteractor(MainModel model, RefRepository<MatchableRef> repository) {
        this.model = model;
        this.repository = repository;
    }

    public MatchableRef createRef(URI uri) {

        //TODO: Show some errors when things don't work out
        String uriScheme = uri.getScheme();
        if (uriScheme.equals("http") || uriScheme.equals("https")) {
            if (isValidLink(uri)) {
                URI tempUri = createTempFile(uri);
                return new SIFTMatchableRef(new File(tempUri));
            }
            return null; //TODO: Show error (suggest to use open image in new tab)
        } else {
            if (isValidFilepath(uri)) {
                return new SIFTMatchableRef(new File(uri));
            } else {
                System.out.println("uriScheme: " + uriScheme + ". Make sure to display an error");
                return null; //TODO: Show error
            }
        }
    }

    private boolean isValidLink(URI link) {
        try {
            URLConnection con = link.toURL().openConnection();
            con.setConnectTimeout((int)TimeUnit.SECONDS.toMillis(5));
            con.setReadTimeout((int)TimeUnit.SECONDS.toMillis(10));
            String contentType = con.getContentType();
            return contentType.toLowerCase().startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isValidFilepath(URI filepath) {
        try {
            String contentType = Files.probeContentType(Paths.get(filepath));
            return contentType.toLowerCase().startsWith("image/");
        } catch (IOException e) {
            return false;
        }
        //TODO: May need to test with ImageIO.read also
    }

    private URI createTempFile(URI uri) {
        String[] uriSplit = uri.toString().split("\\.");
        String ext = uriSplit[uriSplit.length-1];
        try {
            Path tmpPath = Files.createTempFile("", "." + ext);
            InputStream is = uri.toURL().openStream();
            Files.copy(is, tmpPath, StandardCopyOption.REPLACE_EXISTING);
            
            File tmpFile = tmpPath.toFile();
            model.setTmpFile(tmpFile);
            tmpFile.deleteOnExit();

            return tmpPath.toUri();
        } catch (IOException e) {
            return null;
        }
    }

    private File saveTempFile(File tempFile) throws IOException {
        Path appDir = Path.of(System.getProperty("user.home"), ".reflib/savedRefs");
        Files.createDirectories(appDir);
        String destFilename = tempFile.getName();
        File dest = appDir.resolve(destFilename).toFile();

        while (dest.exists()) {
            destFilename = "1" + destFilename;
            dest = appDir.resolve(destFilename).toFile();
        }

        Files.copy(tempFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return dest;
    }

    public void addRef(MatchableRef ref) throws IOException {
        if (model.getTmpFile() != null) {
            if (model.getTmpFile().equals(ref.getFile())) {
                File newFile = saveTempFile(ref.getFile());
                ref.setFile(newFile);
            }
            model.setTmpFile(null);
        }
        
        model.getRefList().add(ref);
        saveRefs();
    }

    public void loadRefs() throws IOException {
        List<MatchableRef> refList = repository.loadRefs();
        model.setRefList(FXCollections.observableArrayList(refList));
    }

    public void saveRefs() throws IOException {
        repository.saveRefs(model.getRefList());
    }
}
