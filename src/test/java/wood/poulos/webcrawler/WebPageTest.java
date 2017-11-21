package wood.poulos.webcrawler;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.TestWebServer;
import wood.poulos.webcrawler.util.URLCreator;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class WebPageTest {

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

    private WebPage indexPage;

    private Collection<WebElement> expectedImages = new HashSet<WebElement>() {{
        add(WebElements.createWebImage(URLCreator.create(host + "images/image1.png")));
        add(WebElements.createWebImage(URLCreator.create(host + "images/image2.png")));
        add(WebElements.createWebImage(URLCreator.create(host + "images/image3.png")));
    }};
    private Collection<WebElement> expectedFiles = new HashSet<WebElement>() {{
        add(WebElements.createWebFile(URLCreator.create(host + "text_files/text_file_1.txt")));
        add(WebElements.createWebFile(URLCreator.create(host + "text_files/text_file_2.txt")));
        add(WebElements.createWebFile(URLCreator.create(host + "text_files/text_file_3.txt")));
    }};
    private Collection<WebElement> expectedPages = new HashSet<WebElement>() {{
        add(WebElements.createWebPage(URLCreator.create(host + "page2.html")));
        add(WebElements.createWebPage(URLCreator.create(host + "page3.html")));
        add(WebElements.createWebPage(URLCreator.create(host + "page4.html")));
    }};

    @BeforeEach
    void setup() throws Exception {
        indexPage = WebElements.createWebPage(URI.create(host + "index.html").toURL());
    }

    @Test
    void testSaveThrowsUnsupportedOperationException() throws Exception {
        assertThrows(UnsupportedOperationException.class,
                () -> WebElements.createWebPage(URI.create("file://.").toURL()).save(Paths.get("./temp.html")));
    }

    @Test
    void testIsCrawledReturnsFalseWhenPageNotCrawled() throws Exception {
        assertFalse(indexPage.isCrawled());
    }

    @Test
    void testIsCrawledReturnsTrueWhenPageHasBeenCrawled() throws Exception {
        indexPage.crawl();
        assertTrue(indexPage.isCrawled());
    }

    @Test
    void testCrawlSingleIndexPageFindsAllImagesFilesAndPages() throws Exception {
        indexPage.crawl();

        assertTrue(indexPage.getFiles().containsAll(expectedFiles));
        assertTrue(indexPage.getImages().containsAll(expectedImages));
        assertTrue(indexPage.getWebPages().containsAll(expectedPages));
    }
}