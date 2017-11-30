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
package wood.poulos.webcrawler.acceptance;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import wood.poulos.webcrawler.WebCrawler;
import wood.poulos.webcrawler.util.FileDownloadVerifier;
import wood.poulos.webcrawler.util.TestPrintStream;
import wood.poulos.webcrawler.util.TestWebServer;
import wood.poulos.webcrawler.util.URLCreator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class WebCrawlerTest {

    private static final String LS = System.getProperty("line.separator");

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

    private Path tempDir;
    private TestPrintStream outContent = new TestPrintStream(System.out, new ByteArrayOutputStream());

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory(Paths.get("."), "tmp");
        tempDir.toFile().deleteOnExit();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() throws Exception {
        FileUtils.deleteDirectory(tempDir.toFile());
    }

    @Test
    void testMainNoArgsDisplayErrorMessage() {
        WebCrawler.main(new String[] {});
        assertTrue(outContent.getLastString().contains("A web address must be specified as the first argument."));
    }

    @Test
    void testMainAny1ArgDisplayErrorMessage() {
        WebCrawler.main(new String[] {""});
        assertTrue(outContent.getLastString().contains("A maximum depth must be specified as the second argument."));
        WebCrawler.main(new String[] {"Hello"});
        assertTrue(outContent.getLastString().contains("A maximum depth must be specified as the second argument."));
        WebCrawler.main(new String[] {"http://google.com/"});
        assertTrue(outContent.getLastString().contains("A maximum depth must be specified as the second argument."));
    }

    @Test
    void testMainAny2ArgsDisplayErrorMessage() {
        WebCrawler.main(new String[] {"", ""});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
        WebCrawler.main(new String[] {"Hello", ""});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
        WebCrawler.main(new String[] {"http://google.com/", ""});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
        WebCrawler.main(new String[] {"", "Hello"});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
        WebCrawler.main(new String[] {"", "2"});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
        WebCrawler.main(new String[] {"Hello", "Hello"});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
        WebCrawler.main(new String[] {"Hello", "2"});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
        WebCrawler.main(new String[] {"http://google.com/", "Hello"});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
        WebCrawler.main(new String[] {"http://google.com/", "2"});
        assertTrue(outContent.getLastString().contains("A local directory must be specified as the third argument."));
    }

    @Test
    void testMainNonURL1stArgDisplayErrorMessage() {
        WebCrawler.main(new String[] {"", "", ""});
        assertTrue(outContent.getLastString().contains("The web address (1st arg) is not formatted correctly or does not represent a web page URL."));
        WebCrawler.main(new String[] {"Hello", "", ""});
        assertTrue(outContent.getLastString().contains("The web address (1st arg) is not formatted correctly or does not represent a web page URL."));
        WebCrawler.main(new String[] {"google.com", "", ""});
        assertTrue(outContent.getLastString().contains("The web address (1st arg) is not formatted correctly or does not represent a web page URL."));
        WebCrawler.main(new String[] {"index.html", "", ""});
        assertTrue(outContent.getLastString().contains("The web address (1st arg) is not formatted correctly or does not represent a web page URL."));
    }

    @Test
    void testMainValidURL1stArgButNonNaturalNumber2ndArgDisplayErrorMessage() {
        WebCrawler.main(new String[] {"http://www.test.com", "", ""});
        assertTrue(outContent.getLastString().contains("The max depth (2nd arg) must be a natural number."));
        WebCrawler.main(new String[] {"http://www.test.com", "Hello", ""});
        assertTrue(outContent.getLastString().contains("The max depth (2nd arg) must be a natural number."));
        WebCrawler.main(new String[] {"http://www.test.com", "-1", ""});
        assertTrue(outContent.getLastString().contains("The max depth (2nd arg) must be a natural number."));
        WebCrawler.main(new String[] {"http://www.test.com", "0", ""});
        assertTrue(outContent.getLastString().contains("The max depth (2nd arg) must be a natural number."));
    }

    @Test
    void testMainValidURL1stArgNaturalNumber2ndArgButInvalidPath3rdArgDisplayErrorMessage() {
        WebCrawler.main(new String[] {"http://www.test.com", "1", "\0"});
        assertTrue(outContent.getLastString().contains("The local directory (3rd arg) must be a file path."));
    }

    @Test
    void testMainValidURL1stArgNaturalNumber2ndArgButNonDirectoryPath3rdArgDisplayErrorMessage() throws IOException {
        Path tempFile = Files.createTempFile(null, null);
        tempFile.toFile().deleteOnExit();
        WebCrawler.main(new String[] {"http://www.test.com", "1", tempFile.toString()});
        assertTrue(outContent.getLastString().contains("The local directory (3rd arg) must be a directory or non-existent."));
    }

    @Test
    void testMainValidURLButNotActualWebsiteDisplayErrorMessage() throws IOException {
        WebCrawler.main(new String[] {"http://www.asdfgaahahafha.xyz/", "1", "temp"});
        String output = outContent.getLastString();
        assertTrue(output.contains("Crawling page at http://www.asdfgaahahafha.xyz/"));
        assertTrue(output.contains("Could not connect to url: http://www.asdfgaahahafha.xyz/"));
    }

    @Test
    void testMainTrustySiteDepth1DownloadsAllFilesAndImagesOnPage() throws Exception {
        WebCrawler.main(new String[] {"http://classics.mit.edu/", "1", tempDir.toString()});
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/ICA-banner-smaller.gif"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/1p.gif"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/browse-icon.gif"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/buy-icon.gif"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/help-icon.gif"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/links-icon.gif"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/macmade.gif"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/search-icon.gif"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(URLCreator.create("http://classics.mit.edu/Images/wa-small.gif"), tempDir);
    }

    @Test
    void testMainLocalSiteDepth3DownalodsAllFilesAndImagesOnAllReachablePages() throws IOException {
        WebCrawler.main(new String[] {host, "3", tempDir.toString()});
        for (int i = 1; i <= 15; i++) {
            wood.poulos.webcrawler.WebCrawlerTest.assertImageDownloaded(i, tempDir, host);
            wood.poulos.webcrawler.WebCrawlerTest.assertFileDownloaded(i, tempDir, host);
        }
    }
}
