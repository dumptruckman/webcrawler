package wood.poulos.webcrawler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLConverter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileRepositoryTest {

    static final WebElement RELIABLE_IMAGE;

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
    void setup() throws Exception {
        repo = new LocalFileRepository(Files.createTempDirectory("."));
        elements = getStagedElements(repo);
    }

    static Collection<WebElement> getStagedElements(WebElementRepository repo) {
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
        assertFalse(getStagedElements(repo).isEmpty());
    }

    @Test
    void testAddElementsMakeGetStagedElementsContainsAddedElement() throws Exception {
        WebElement e = WebElements.createWebPage(URI.create("file://./test.index").toURL());
        repo.addElement(e);
        assertTrue(getStagedElements(repo).contains(e));
    }

    @Test
    void testRemoveElementsMakesStagedElementsEmptyAfterAdding() throws Exception {
        WebElement element = RELIABLE_IMAGE;
        repo.addElement(element);
        elements = getStagedElements(repo);
        assertFalse(elements.isEmpty());
        repo.removeElement(element);
        elements = getStagedElements(repo);
        assertTrue(elements.isEmpty());
    }

    @Test
    void testGetLocalPathForElementWhenDownloadLocationSetToCurrentDirectory() throws Exception {
        repo = new LocalFileRepository(Paths.get("."));
        WebElement element = RELIABLE_IMAGE;
        repo.addElement(element);
        Path currentDirectory = Paths.get(".");
        Path expectedPath = currentDirectory.resolve(URLConverter.convertToFilePath(element.getURL()));
        assertEquals(expectedPath, repo.getLocalPathForElement(element));
    }
}