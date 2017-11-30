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
import wood.poulos.webcrawler.util.URLCreator;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

import static org.junit.jupiter.api.Assertions.*;

public class WebCrawlerTest {

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

    private LocalFileRepository repo;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory(Paths.get("."), "tmp");
        tempDir.toFile().deleteOnExit();
        repo = new LocalFileRepository(tempDir);
    }

    @AfterEach
    void tearDown() throws Exception {
        FileUtils.deleteDirectory(tempDir.toFile());
    }

    @Test
    void testVerifySufficientArgCount() {
        WebCrawler.verifySufficientArgCount(new String[] {"1", "2", "3"});
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {"1", "2"}));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {"1"}));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {}));
    }

    @Test
    void testGetValidURIIllegalURIThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.parseValidURL("c:\\"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testGetValidURILegalURI() {
        assertTrue(WebCrawler.parseValidURL("https://www.google.com") instanceof URI);
    }

    @Test
    void testGetValidMaxDepthNonNumberThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.parseValidMaxDepth("asdf"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.parseValidMaxDepth("1b"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.parseValidMaxDepth("--1"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.parseValidMaxDepth("."));
    }

    @Test
    void testGetValidMaxDepthNegativeNumberThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.parseValidMaxDepth("-1"));
    }

    @Test
    void testGetValidMaxDepthZeroThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.parseValidMaxDepth("0"));
    }

    @Test
    void testGetValidMaxDepthValidNumbers() {
        assertEquals(1, WebCrawler.parseValidMaxDepth("1"));
        assertEquals(2, WebCrawler.parseValidMaxDepth("2"));
        assertEquals(10, WebCrawler.parseValidMaxDepth("10"));
    }

    @Test
    void testGetValidDownloadRepositoryInvalidPathThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.parseValidDownloadRepository("/\\"));
    }

    @Test
    void testGetValidDownloadRepositoryValidPath() {
        WebCrawler.parseValidDownloadRepository(".");
    }

    @Test
    void testVerifyValidDownloadRepositoryNonDirectoryThrowsIAE() throws Exception {
        Path p = Files.createTempFile(".", "tmp");
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifyValidDownloadRepository(p));
    }

    @Test
    void testVerifyValidDownloadRepositoryNonWritableDirectoryThrowsIAE() throws Exception {
        if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
            Path p = Files.createTempDirectory("tmp", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("r-xr-x---")));
            assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifyValidDownloadRepository(p));
        }
    }

    @Test
    void testStartTestPagesDepth1() throws IOException {
        WebCrawler crawler = new WebCrawler(URI.create(host), 1, repo);
        crawler.start();
        for (int i = 1; i <= 3; i++) {
            assertImageDownloaded(i, tempDir, host);
            assertFileDownloaded(i, tempDir, host);
        }
    }

    @Test
    void testStartTestPagesDepth2() throws IOException {
        WebCrawler crawler = new WebCrawler(URI.create(host), 2, repo);
        crawler.start();
        for (int i = 1; i <= 12; i++) {
            assertImageDownloaded(i, tempDir, host);
            assertFileDownloaded(i, tempDir, host);
        }
    }

    @Test
    void testStartTestPagesDepth3() throws IOException {
        WebCrawler crawler = new WebCrawler(URI.create(host), 3, repo);
        crawler.start();
        for (int i = 1; i <= 15; i++) {
            assertImageDownloaded(i, tempDir, host);
            assertFileDownloaded(i, tempDir, host);
        }
    }

    public static void assertImageDownloaded(int imageNumber, Path tempDir, String host) throws IOException {
        FileDownloadVerifier.assertFileDownloadedSuccessfully(
                Paths.get("./testPages/images/image" + imageNumber + ".png"),
                URLCreator.create(host + "images/image" + imageNumber + ".png"), tempDir);
    }

    public static void assertFileDownloaded(int fileNumber, Path tempDir, String host) throws IOException {
        FileDownloadVerifier.assertFileDownloadedSuccessfully(
                Paths.get("./testPages/text_files/text_file_" + fileNumber + ".txt"),
                URLCreator.create(host + "text_files/text_file_" + fileNumber + ".txt"), tempDir);
    }
}