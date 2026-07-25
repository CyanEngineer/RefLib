package com.cyaneer.reflib;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.cyaneer.reflib.domain.MatchableRef;

public class MainInteractorTest {
    
    @Test
    public void testCreateNewRefCreatesMatchableRefFromFile() {
        MainModel model = new MainModel();
        MainInteractor interactor = new MainInteractor(model, null);

        URI uri = Paths.get("src/test/resources/testimage.png").toAbsolutePath().toUri();

        MatchableRef ref = null;

        try {
            ref = interactor.createRef(uri);
        } catch (IOException e) {

        }

        assert(ref != null);
        assert(ref.getFile().getAbsolutePath().endsWith("src/test/resources/testimage.png"));
    }

    @Test
    public void testCreateNewRefThrowsErrorIfNotImageFile() {
        MainModel model = new MainModel();
        MainInteractor interactor = new MainInteractor(model, null);

        URI uri = Paths.get("src/test/resources/notimage.md").toAbsolutePath().toUri();

        assertThrows(IOException.class, () -> interactor.createRef(uri));
    }

    @Test
    public void testCreateNewRefThrowsErrorIfNotImageLink() throws URISyntaxException {
        MainModel model = new MainModel();
        MainInteractor interactor = new MainInteractor(model, null);

        URI uri = new URI("https://cyaneer.com/");

        assertThrows(IOException.class, () -> interactor.createRef(uri));
    }
}
