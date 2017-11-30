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

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility for copying the contents of a URL to a file path.
 */
public class URLDownloader {

    /**
     * Copies the contents of a URL to a location on disc as given by a path.
     *
     * @param fromURL The URL to download from.
     * @param toPath  The path to download to.
     * @throws IOException If something goes wrong while trying to copy the
     *                     contents.
     */
    public static void downloadElement(@NotNull URL fromURL, @NotNull Path toPath) throws IOException {
        createParentDirsIfNonExistent(toPath);
        performCopy(openConnection(fromURL), toPath);
    }

    private static void createParentDirsIfNonExistent(@NotNull Path path) throws IOException {
        verifyPathHasParent(path);
        path.getParent().toFile().mkdirs();
    }

    private static void verifyPathHasParent(@NotNull Path path) throws IOException {
        Path parentDir = path.getParent();
        if (parentDir == null) {
            throw new IOException(path + " has no parent directory");
        }
    }

    @NotNull
    private static URLConnection openConnection(@NotNull URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.connect();
        return connection;
    }

    private static void performCopy(@NotNull URLConnection copyFrom, @NotNull Path copyTo) throws IOException {
        try (InputStream inStream = copyFrom.getInputStream();
             OutputStream outStream = Files.newOutputStream(copyTo)) {
            IOUtils.copyLarge(inStream, outStream);
        }
    }
}
