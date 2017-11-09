package wood.poulos.webcrawler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLConverter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileRepositoryTest {

    private static final WebElement RELIABLE_IMAGE;

    static {
        try {
            // A URL that hasn't changed since 1994.
            URL reliableImageURL = URI.create("http://classics.mit.edu/Images/ICA-banner-smaller.gif").toURL();
            RELIABLE_IMAGE = WebElements.createWebImage(reliableImageURL);
        } catch (MalformedURLException e) {
            // Rethrow as a runtime exception this since its using a static url which would make this a programmer
            // error.
            throw new RuntimeException(e);
        }
    }

    private LocalFileRepository repo;

    private Collection<WebElement> elements;

    @BeforeEach
    void setup() {
        repo = new DefaultLocalFileRepository();
        elements = getStagedElements();
    }

    private Collection<WebElement> getStagedElements() {
        Collection<WebElement> result = new ArrayList<>();
        for (WebElement e : repo.getStagedElements()) {
            result.add(e);
        }
        return result;
    }

    @Test
    void testGetStagedElementsEmptyByDefault() {
        assertTrue(elements.isEmpty());
    }

    @Test
    void testAddElementsMakeGetStagedElementsNonEmpty() throws Exception {
        repo.addElement(WebElements.createWebPage(URI.create("file://./test.index").toURL()));
        assertFalse(elements.isEmpty());
    }

    @Test
    void testRemoveElementsMakesStagedElementsEmptyAfterAdding() throws Exception {
        WebElement element = RELIABLE_IMAGE;
        repo.addElement(element);
        elements = getStagedElements();
        assertFalse(elements.isEmpty());
        repo.removeElement(element);
        elements = getStagedElements();
        assertTrue(elements.isEmpty());
    }

    @Test
    void testGetLocalPathForElementWhenDownloadLocationSetToCurrentDirectory() throws Exception {
        WebElement element = RELIABLE_IMAGE;
        repo.addElement(element);
        Path currentDirectory = Paths.get(".");
        repo.setLocalFileLocation(currentDirectory);
        Path expectedPath = currentDirectory.resolve(URLConverter.convertToFilePath(element.getURL()));
        assertEquals(expectedPath, repo.getLocalPathForElement(element));
    }
}