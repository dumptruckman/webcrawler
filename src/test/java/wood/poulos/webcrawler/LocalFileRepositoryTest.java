/*
 * MIT License
 *
 * Copyright (c) 2017 Jeremy Wood, Elijah Poulos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package wood.poulos.webcrawler;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
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
    private static TestWebServer server;

    @BeforeAll
    static void setUpWebServer() throws IOException {
        TestWebServer.lock.lock();
        server = new TestWebServer();
        host = "http://localhost:" + server.getPort() + "/";
        server.start();
    }

    @AfterAll
    static void tearDownWebServer() {
        server.stop();
        TestWebServer.lock.unlock();
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
        repo = new LocalFileRepository(tempDir);
        elements = getStagedElements(repo);
    }

    @AfterEach
    void tearDown() throws Exception {
        FileUtils.deleteDirectory(tempDir.toFile());
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
        repo.addElement(WebElements.createWebPage(URI.create("file://./test.html").toURL()));
        assertFalse(getStagedElements(repo).isEmpty());
    }

    @Test
    void testAddElementsMakeGetStagedElementsContainsAddedElement() throws Exception {
        WebElement e = WebElements.createWebPage(URI.create("file://./test.html").toURL());
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
        URL imageURL = URLCreator.create(host + "images/image1.png");
        WebElement image = WebElements.createWebImage(imageURL);
        repo.addElement(image);
        repo.commit();
        FileDownloadVerifier.assertFileDownloadedSuccessfully(Paths.get("./testPages/images/image1.png"), imageURL, tempDir);
    }

    @Test
    void testCommitTestFileDownloadsSuccessfully() throws IOException {
        URL fileURL = URLCreator.create(host + "text_files/text_file_1.txt");
        WebElement file = WebElements.createWebFile(fileURL);
        repo.addElement(file);
        repo.commit();
        FileDownloadVerifier.assertFileDownloadedSuccessfully(Paths.get("./testPages/text_files/text_file_1.txt"), fileURL, tempDir);
    }

    @Test
    void testCommitWebPageDownloadsNothing() {
        URL pageURL = URLCreator.create(host + "index.html");
        WebElement page = WebElements.createWebPage(pageURL);
        repo.addElement(page);
        repo.commit();
        File downloaded = tempDir.resolve(URLConverter.convertToFilePath(pageURL).toString()).toFile();
        assertFalse(downloaded.exists());
    }

    @Test
    void testCommitWebPageCreatesDownloadDirectoryIfNonExistent() throws IOException {
        URL pageURL = URLCreator.create(host + "images/image1.png");
        WebElement element = WebElements.createWebImage(pageURL);
        Path tempDir = Paths.get("asdfighairgjaorgjaofgj");
        repo = new LocalFileRepository(tempDir);
        repo.addElement(element);
        repo.commit();

        File downloaded = tempDir.resolve(URLConverter.convertToFilePath(pageURL).toString()).toFile();
        assertTrue(downloaded.exists());

        FileUtils.deleteDirectory(tempDir.toFile());
    }
}