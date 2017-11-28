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
     * Converts the given URL into a path that is suitable for any popular file system.
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
