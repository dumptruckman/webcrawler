package wood.poulos.webcrawler.util;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileDownloadVerifier {

    public static void assertFileDownloadedSuccessfully(@NotNull Path original, @NotNull URL url, @NotNull Path tempDir) throws IOException {
        Path downloaded = tempDir.resolve(URLConverter.convertToFilePath(url));

        assertTrue(original.toFile().exists(), "Original file does not exist: " + original);
        assertTrue(downloaded.toFile().exists(), "Downloaded file does not exist: " + downloaded);

        assertTrue(FileUtils.contentEquals(original.toFile(), downloaded.toFile()), "File contents are not equal");
    }

    public static void assertFileDownloadedSuccessfully(@NotNull URL url, @NotNull Path tempDir) throws IOException {
        Path originalTempDir = Files.createTempDirectory(null);
        originalTempDir.toFile().deleteOnExit();
        Path original = originalTempDir.resolve(URLConverter.convertToFilePath(url));

        URLDownloader.downloadElement(url, original);

        assertFileDownloadedSuccessfully(original, url, tempDir);
    }
}
