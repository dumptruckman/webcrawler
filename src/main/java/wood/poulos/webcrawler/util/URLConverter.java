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

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A utility for converting URLs into other types.
 */
public class URLConverter {

    /**
     * Converts the given URL into a path that is suitable for any popular file
     * system.
     *
     * @param url The URL to convert.
     * @return A valid path for any popular file system.
     */
    @NotNull
    public static Path convertToFilePath(@NotNull URL url) {
        String simplifiedURL = simplifyURL(url);
        String encodedURL = encodeSimplifiedURL(simplifiedURL);
        return Paths.get(encodedURL);
    }

    @NotNull
    private static String simplifyURL(@NotNull URL url) {
        return url.getHost() + parseURLPort(url) + url.getPath();
    }

    @NotNull
    private static String parseURLPort(@NotNull URL url) {
        int port = url.getPort();
        return port != -1 ? ":" + port : "";
    }

    @NotNull
    private static String encodeSimplifiedURL(@NotNull String simplifiedURL) {
        try {
            return URLEncoder.encode(simplifiedURL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Rethrow as a runtime exception this since its using a very universal encoding.
            throw new RuntimeException(e);
        }
    }
}
