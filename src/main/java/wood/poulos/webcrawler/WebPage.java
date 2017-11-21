package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A abstract representation of a web page.
 */
public class WebPage extends AbstractWebElement implements WebElement {


    /**
     * A RegEx pattern to find and differentiate links and images within HTML.
     */
    final static Pattern LINK_AND_IMAGE_PATTERN = Pattern.compile("(?:(?:<a.*?href\\s*=\\s*(?:\"(.+?)\"|'(.+?)').*?>)|(?:<img.*?src\\s*=\\s*(?:\"(.+?)\"|'(.+?)').*?>))");

    /**
     * Creates a WebPage object for the given URL.
     *
     * @param url the URL to create the WebPage for.
     */
    WebPage(URL url) {
        super(url);
    }

    /**
     * Parses the concrete web page this WebPage represents and identifies all of its files, images, and links to other
     * pages.
     * <p>
     * This must be done before using this class's various getters.
     * </p>
     *
     * @throws IOException If there is trouble connecting to the URL.
     */
    void crawl() throws IOException {
        URLConnection conn = getURL().openConnection();
        conn.connect();
        Scanner input = new Scanner(conn.getInputStream());
        while (input.hasNext(LINK_AND_IMAGE_PATTERN)) {
            String next = input.next(LINK_AND_IMAGE_PATTERN);
            Matcher matcher = LINK_AND_IMAGE_PATTERN.matcher(next);
            if (!matcher.matches()) {
                throw new IllegalStateException("Matcher failed to match");
            }

        }


        /* TODO
        This is going to require the most effort out of everything most likely. We will probably need several
        more classes to deal with this in a clean way.
         */
    }

    /**
     * Used internally to determine if the page has already been crawled.
     *
     * @return True if the page has already been crawled.
     */
    boolean isCrawled() {
        return false;
    }

    /**
     * Returns an unmodifiable collection of the images located on this page.
     *
     * @return an unmodifiable collection of the images located on this page.
     */
    @NotNull
    public Collection<WebImage> getImages() {
        return null;
    }

    /**
     * Returns an unmodifiable collection of the files located on this page.
     *
     * @return an unmodifiable collection of the files located on this page.
     */
    @NotNull
    public Collection<WebFile> getFiles() {
        return null;
    }

    /**
     * Returns an unmodifiable collection of the links to other web pages located on this page.
     *
     * @return an unmodifiable collection of the links to other web pages located on this page.
     */
    @NotNull
    public Collection<WebPage> getWebPages() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param saveLocation
     */
    @Override
    public void save(Path saveLocation) {
        throw new UnsupportedOperationException("page doesn't support save");
    }

    /**
     * An internal utility for differentiating regex matches of URLs into the 3 relevant types: Page, File and Image.
     */
    abstract static class URLParser {

        /**
         * Creates a an appropriate type of URLParser to parse the URL from the given matcher.
         * <p>
         *     The given matcher must have already matched the pattern {@link WebPage#LINK_AND_IMAGE_PATTERN}.
         * </p>
         * @param matcher The matcher than will be parsed into an appropriate URL.
         * @return A new URLParse of the appropriate type.
         */
        @NotNull
        static WebPage.URLParser fromMatcher(@NotNull Matcher matcher) {
            String group1 = matcher.group(1);
            String group2 = matcher.group(2);
            String group3 = matcher.group(3);
            String group4 = matcher.group(4);


            if (group1 != null || group2 != null) {
                return new LinkURLParser(group1 != null ? group1 : group2);
            } else if (group3 != null || group4 != null) {
                return new ImageURLParser(group3 != null ? group3 : group4);
            } else {
                throw new IllegalArgumentException("Matcher has all empty capture groups.");
            }
        }

        @NotNull
        protected final URI uri;

        /**
         * Creates a new URL parser for the given string which should be a valid URI.
         *
         * @param urlString The URL string.
         */
        URLParser(@NotNull String urlString) {
            this.uri = URI.create(urlString);
        }

        /**
         * Returns the type of the URL this URLParser handles.
         *
         * @return The type of the URL this URLParser handles.
         */
        @NotNull
        abstract URLType getURLType();

        /**
         * Resolves this URLParser's parsed URL against the given parent URL.
         *
         * If this URLParser's parsed URL is relative, it will inserted into the correct position of the given parent
         * URL and returned as the result. If this URLParser's parsed URL is absolute, it will simply be returned.
         *
         * @param parent The parent URL to resolve against.
         * @return The resolved URL.
         * @throws URISyntaxException If either this URLParser's parsed URL or the given parent URL cannot be
         * parsed as a valid URI.
         * @throws MalformedURLException If either URL does not include the protocol.
         */
        @NotNull
        URL resolveURL(@NotNull URL parent) throws URISyntaxException, MalformedURLException {
            URI resolved = parent.toURI().resolve(uri);
            resolved = fixDeepRelativity(resolved);
            return resolved.toURL();
        }

        /**
         * Fixes a bug in {@link URI#resolve(URI)} by removing an extra parent directory (../) found when resolving
         * a URL that backs up more than 1 parent directory.
         *
         * @param uri The URI to fix.
         * @return The fixed URI.
         */
        @NotNull
        private URI fixDeepRelativity(@NotNull URI uri) {
            String uriString = uri.toString();
            if (uriString.contains("../")) {
                uriString = uriString.replace("../", "");
                uri = URI.create(uriString);
            }
            return uri;
        }

        /**
         * A type to indicate what this URLParser's URL represents: another webpage, a non-page file, or an image.
         */
        enum URLType {
            PAGE, FILE, IMAGE;
        }

    }

    /**
     * Provides specific parsing for non-image URLs.
     */
    static class LinkURLParser extends URLParser {

        //private final static Pattern PAGE_PATTERN = Pattern.compile("<\\s*a.*?href\\s*=\\s*(?:\"|')(.*\\.html?|.*\\.asp|.*\\.cgi|.*\\.php|[^.]*?|.*\\/)(?:\"|').*?>");
        private final static Pattern PAGE_PATTERN = Pattern.compile("^(.*\\.html?|.*\\.asp|.*\\.cgi|.*\\.php|[\\w\\d\\/]+|.+\\/)$");

        @NotNull
        private final URLType urlType;

        private LinkURLParser(@NotNull String urlString) {
            super(urlString);
            String path = uri.getPath();
            if (path != null) {
                Matcher pageMatcher = PAGE_PATTERN.matcher(path);
                if (pageMatcher.find()) {
                    urlType = URLType.PAGE;
                } else {
                    urlType = URLType.FILE;
                }
            } else {
                urlType = URLType.PAGE;
            }
        }

        /** {@inheritDoc} */
        @Override
        @NotNull
        URLType getURLType() {
            return urlType;
        }
    }

    /**
     * Provides specific parsing for image URLs.
     */
    static class ImageURLParser extends URLParser {

        private ImageURLParser(@NotNull String urlString) {
            super(urlString);
        }

        /** {@inheritDoc} */
        @Override
        @NotNull
        URLType getURLType() {
            return URLType.IMAGE;
        }

    }
}
