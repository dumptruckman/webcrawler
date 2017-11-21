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

class WebCrawlerTest {

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
    public void testVerifySufficientArgCount() {
        WebCrawler.verifySufficientArgCount(new String[] {"1", "2", "3"});
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {"1", "2"}));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {"1"}));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {}));
    }

    @Test
    public void testGetValidURIIllegalURIThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidURI("c:\\"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testGetValidURILegalURI() {
        assertTrue(WebCrawler.getValidURI("https://www.google.com") instanceof URI);
        assertTrue(WebCrawler.getValidURI("c:/") instanceof URI);
    }

    @Test
    public void testGetValidMaxDepthNonNumberThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("asdf"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("1b"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("--1"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("."));
    }

    @Test
    public void testGetValidMaxDepthNegativeNumberThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("-1"));
    }

    @Test
    public void testGetValidMaxDepthZeroThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("0"));
    }

    @Test
    public void testGetValidMaxDepthValidNumbers() {
        assertEquals(1, WebCrawler.getValidMaxDepth("1"));
        assertEquals(2, WebCrawler.getValidMaxDepth("2"));
        assertEquals(10, WebCrawler.getValidMaxDepth("10"));
    }

    @Test
    public void testGetValidDownloadRepositoryInvalidPathThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidDownloadRepository("/\\"));
    }

    @Test
    public void testGetValidDownloadRepositoryValidPath() {
        WebCrawler.getValidDownloadRepository(".");
    }

    @Test
    public void testVerifyValidDownloadRepositoryNonDirectoryThrowsIAE() throws Exception {
        Path p = Files.createTempFile(".", "tmp");
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifyValidDownloadRepository(p));
    }

    @Test
    public void testVerifyValidDownloadRepositoryNonWritableDirectoryThrowsIAE() throws Exception {
        if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
            Path p = Files.createTempDirectory("tmp", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("r-xr-x---")));
            assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifyValidDownloadRepository(p));
        }
    }

    @Test
    public void testStartTestPagesDepth1() throws IOException {
        WebCrawler crawler = new WebCrawler(URI.create(host), 1, repo);
        crawler.start();
        FileDownloadVerifier.assertFileDownloadedSuccessfully(Paths.get("./testPages/images/image1.png"), URLCreator.create(host + "images/image1.png"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(Paths.get("./testPages/images/image2.png"), URLCreator.create(host + "images/image2.png"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(Paths.get("./testPages/images/image3.png"), URLCreator.create(host + "images/image3.png"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(Paths.get("./testPages/text_files/text_file_1.txt"), URLCreator.create(host + "text_files/text_file_1.txt"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(Paths.get("./testPages/text_files/text_file_2.txt"), URLCreator.create(host + "text_files/text_file_2.txt"), tempDir);
        FileDownloadVerifier.assertFileDownloadedSuccessfully(Paths.get("./testPages/text_files/text_file_3.txt"), URLCreator.create(host + "text_files/text_file_3.txt"), tempDir);
    }
}