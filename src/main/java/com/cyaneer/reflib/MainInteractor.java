package com.cyaneer.reflib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

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

    public MatchableRef createRef(URI uri) throws IOException {
        String uriScheme = uri.getScheme();
        if (uriScheme.equals("http") || uriScheme.equals("https")) {
            if (isValidLink(uri)) {
                URI tempUri = createTempFile(uri);
                return new SIFTMatchableRef(new File(tempUri));
            } else {
                throw new IOException(uri.toString() + " is not a valid image link"); //TODO: Test
            }
        } else {
            if (isValidImageFile(uri)) {
                return new SIFTMatchableRef(new File(uri));
            } else {
                throw new IOException(uri.toString() + " is not a valid image file"); //TODO: Test
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

    private boolean isValidImageFile(URI filepath) {
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(new File(filepath));
            if (iis == null) {
                return false;
            }

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            boolean isValid = readers.hasNext();
            iis.close();
            return isValid;            
        } catch (IOException e) {
            return false;
        }
    }

    private URI createTempFile(URI uri) throws IOException {
        String[] uriSplit = uri.toString().split("\\.");
        String ext = uriSplit[uriSplit.length-1];
        
        Path tmpPath = Files.createTempFile("", "." + ext);
        InputStream is = uri.toURL().openStream();
        Files.copy(is, tmpPath, StandardCopyOption.REPLACE_EXISTING);
        
        File tmpFile = tmpPath.toFile();
        model.setTmpFile(tmpFile);
        tmpFile.deleteOnExit();

        return tmpPath.toUri();
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
