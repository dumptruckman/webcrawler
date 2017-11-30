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

        assertTrue(indexPage.getWebPages().containsAll(expectedPages));
        assertTrue(indexPage.getFiles().containsAll(expectedFiles));
        assertTrue(indexPage.getImages().containsAll(expectedImages));
    }
}