package wood.poulos.webcrawler;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.FileDownloadVerifier;
import wood.poulos.webcrawler.util.TestWebServer;
import wood.poulos.webcrawler.util.URLConverter;
import wood.poulos.webcrawler.util.URLCreator;

import java.io.File;
import java.io.IOException;
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

    private static String host;

    @BeforeAll
    static void setUpWebServer() throws IOException {
        TestWebServer server = new TestWebServer();
        host = "http://localhost:" + server.getPort() + "/";
        server.start();
    }

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

    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory(Paths.get("."), "tmp");
        tempDir.toFile().deleteOnExit();
        repo = new LocalFileRepository(tempDir);
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

    @Test
    void testCommitTestImageDownloadsSuccessfully() throws IOException {
        URL imageURL = URLCreator.create(host + "images/images1.png");
        WebElement image = WebElements.createWebImage(imageURL);
        repo.addElement(image);
        repo.commit();
        assertTrue(FileDownloadVerifier.isFileDownloadedSuccessfully(imageURL, tempDir));
    }

    @Test
    void testCommitTestFileDownloadsSuccessfully() throws IOException {
        URL fileURL = URLCreator.create(host + "text_files/text_file_1.txt");
        WebElement file = WebElements.createWebImage(fileURL);
        repo.addElement(file);
        repo.commit();
        assertTrue(FileDownloadVerifier.isFileDownloadedSuccessfully(fileURL, tempDir));
    }

    @Test
    void testCommitWebPageDownloadsNothing() {
        URL pageURL = URLCreator.create(host + "index.html");
        WebElement page = WebElements.createWebImage(pageURL);
        repo.addElement(page);
        repo.commit();
        File downloaded = tempDir.resolve(URLConverter.convertToFilePath(pageURL).toString()).toFile();
        assertFalse(downloaded.exists());
    }
}