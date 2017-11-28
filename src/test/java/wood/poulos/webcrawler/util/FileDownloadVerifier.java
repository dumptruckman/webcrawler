package wood.poulos.webcrawler.util;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileDownloadVerifier {

    public static void assertFileDownloadedSuccessfully(@NotNull Path original, @NotNull URL url, @NotNull Path tempDir) throws IOException {
        Path downloaded = tempDir.resolve(URLConverter.convertToFilePath(url));

        assertTrue(original.toFile().exists(), "Original file does not exist: " + original);
        assertTrue(downloaded.toFile().exists(), "Downloaded file does not exist: " + downloaded);

//        try (FileInputStream originalStream = new FileInputStream(original.toFile());
//             FileInputStream downloadedStream = new FileInputStream(downloaded.toFile())) {
//            int b;
//            int i = 0;
//            while ((b = originalStream.read()) != -1) {
//                assertEquals(b, downloadedStream.read(), "Bytes not equal at index " + i);
//                i++;
//            }
//        }
        assertTrue(FileUtils.contentEquals(original.toFile(), downloaded.toFile()), "File contents are not equal");
    }
}
