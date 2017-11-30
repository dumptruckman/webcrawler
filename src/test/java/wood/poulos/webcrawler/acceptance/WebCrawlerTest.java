package wood.poulos.webcrawler.acceptance;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import wood.poulos.webcrawler.WebCrawler;
import wood.poulos.webcrawler.util.FileDownloadVerifier;
import wood.poulos.webcrawler.util.TestPrintStream;
import wood.poulos.webcrawler.util.TestWebServer;
import wood.poulos.webcrawler.util.URLConverter;
import wood.poulos.webcrawler.util.URLCreator;
import wood.poulos.webcrawler.util.URLDownloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
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
        assertEquals("A web address must be specified as the first argument." + LS, outContent.getLastString());
    }

    @Test
    void testMainAny1ArgDisplayErrorMessage() {
        WebCrawler.main(new String[] {""});
        assertEquals("A maximum depth must be specified as the second argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"Hello"});
        assertEquals("A maximum depth must be specified as the second argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"http://google.com/"});
        assertEquals("A maximum depth must be specified as the second argument." + LS, outContent.getLastString());
    }

    @Test
    void testMainAny2ArgsDisplayErrorMessage() {
        WebCrawler.main(new String[] {"", ""});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"Hello", ""});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"http://google.com/", ""});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"", "Hello"});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"", "2"});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"Hello", "Hello"});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"Hello", "2"});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"http://google.com/", "Hello"});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"http://google.com/", "2"});
        assertEquals("A local directory must be specified as the third argument." + LS, outContent.getLastString());
    }

    @Test
    void testMainNonURL1stArgDisplayErrorMessage() {
        WebCrawler.main(new String[] {"", "", ""});
        assertEquals("The web address (1st arg) is not formatted correctly or does not represent a web page URL." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"Hello", "", ""});
        assertEquals("The web address (1st arg) is not formatted correctly or does not represent a web page URL." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"google.com", "", ""});
        assertEquals("The web address (1st arg) is not formatted correctly or does not represent a web page URL." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"index.html", "", ""});
        assertEquals("The web address (1st arg) is not formatted correctly or does not represent a web page URL." + LS, outContent.getLastString());
    }

    @Test
    void testMainValidURL1stArgButNonNaturalNumber2ndArgDisplayErrorMessage() {
        WebCrawler.main(new String[] {"http://www.test.com", "", ""});
        assertEquals("The max depth (2nd arg) must be a natural number." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"http://www.test.com", "Hello", ""});
        assertEquals("The max depth (2nd arg) must be a natural number." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"http://www.test.com", "-1", ""});
        assertEquals("The max depth (2nd arg) must be a natural number." + LS, outContent.getLastString());
        WebCrawler.main(new String[] {"http://www.test.com", "0", ""});
        assertEquals("The max depth (2nd arg) must be a natural number." + LS, outContent.getLastString());
    }

    @Test
    void testMainValidURL1stArgNaturalNumber2ndArgButInvalidPath3rdArgDisplayErrorMessage() {
        WebCrawler.main(new String[] {"http://www.test.com", "1", "\0"});
        assertEquals("The local directory (3rd arg) must be a file path." + LS, outContent.getLastString());
    }

    @Test
    void testMainValidURL1stArgNaturalNumber2ndArgButNonDirectoryPath3rdArgDisplayErrorMessage() throws IOException {
        Path tempFile = Files.createTempFile(null, null);
        tempFile.toFile().deleteOnExit();
        WebCrawler.main(new String[] {"http://www.test.com", "1", tempFile.toString()});
        assertEquals("The local directory (3rd arg) must be a directory or non-existent." + LS, outContent.getLastString());
    }

    @Test
    void testMainValidURLButNotActualWebsiteDisplayErrorMessage() throws IOException {
        WebCrawler.main(new String[] {"http://www.asdfgaahahafha.xyz/", "1", "temp"});
        assertEquals("Crawling page at http://www.asdfgaahahafha.xyz/" + LS + "Could not connect to url: http://www.asdfgaahahafha.xyz/" + LS, outContent.getLastString());
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
