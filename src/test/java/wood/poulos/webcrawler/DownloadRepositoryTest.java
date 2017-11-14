package wood.poulos.webcrawler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLConverter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static wood.poulos.webcrawler.LocalFileRepositoryTest.RELIABLE_IMAGE;
import static wood.poulos.webcrawler.LocalFileRepositoryTest.getStagedElements;

class DownloadRepositoryTest {

    DownloadRepository repo = DownloadRepository.INSTANCE;

    @BeforeEach
    public void setup() {
        repo.setDownloadLocation(Paths.get("."));
        Collection<WebElement> elements = getStagedElements(repo);
        for (WebElement e : elements) {
            repo.removeElement(e);
        }
    }

    @Test
    void testSetDownloadLocationPreservesAddedElements() {
        Collection<WebElement> elements = getStagedElements(repo);
        assertTrue(elements.isEmpty());
        repo.setDownloadLocation(Paths.get("./tmp"));
        elements = getStagedElements(repo);
        assertTrue(elements.isEmpty());

        repo.addElement(RELIABLE_IMAGE);
        elements = getStagedElements(repo);
        assertTrue(elements.contains(RELIABLE_IMAGE));
        repo.setDownloadLocation(Paths.get("./tmp2"));
        elements = getStagedElements(repo);
        assertTrue(elements.contains(RELIABLE_IMAGE));
    }

    @Test
    void testGetLocalPathForElementWhenDownloadLocationSetToCurrentDirectory() throws Exception {
        repo.setDownloadLocation(Paths.get("."));
        WebElement element = RELIABLE_IMAGE;
        repo.addElement(element);
        Path currentDirectory = Paths.get(".");
        Path expectedPath = currentDirectory.resolve(URLConverter.convertToFilePath(element.getURL()));
        assertEquals(expectedPath, repo.getLocalPathForElement(element));
    }

    @Test
    void testGetLocalPathForElementWhenDownloadLocationChanged() throws Exception {
        repo.setDownloadLocation(Paths.get("."));
        WebElement element = RELIABLE_IMAGE;
        repo.addElement(element);
        Path currentDirectory = Paths.get(".");
        Path expectedPath = currentDirectory.resolve(URLConverter.convertToFilePath(element.getURL()));
        assertEquals(expectedPath, repo.getLocalPathForElement(element));

        repo.setDownloadLocation(Paths.get("./tmp"));
        Path tempDirectory = Paths.get("./tmp");
        expectedPath = tempDirectory.resolve(URLConverter.convertToFilePath(element.getURL()));
        assertEquals(expectedPath, repo.getLocalPathForElement(element));
    }

}