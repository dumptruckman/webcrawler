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
