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


    final static Pattern LINK_AND_IMAGE_PATTERN = Pattern.compile("(?:(?:<a.*?href\\s*=\\s*(?:\"(.+?)\"|'(.+?)').*?>)|(?:<img.*?src\\s*=\\s*(?:\"(.+?)\"|'(.+?)').*?>))");


    public WebPage(URL url) {
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
        private final URI uri;

        /**
         * Creates a new URL parser for the given string which should be a valid URI.
         *
         * @param urlString The URL string.
         */
        URLParser(@NotNull String urlString) {
            this.uri = URI.create(urlString);
        }

        /**
         * The type of the URL this URLParser handles.
         *
         * @return
         */
        @NotNull
        abstract URLType getURLType();

        @NotNull URL resolveURL(@NotNull URL parent) throws URISyntaxException, MalformedURLException {
            return parent.toURI().resolve(uri).toURL();
        }

        enum URLType {
            PAGE, FILE, IMAGE;
        }

    }

    static class LinkURLParser extends URLParser {

        private final static Pattern PAGE_PATTERN = Pattern.compile("<\\s*a.*?href\\s*=\\s*(?:\"|')(.*html?|.*asp?|.*cgi?|.*php?|.*?)(?:\"|')*>(.*?)<\\s*?\\/\\s*?a\\s*?>");


        @NotNull
        private final URLType urlType;

        LinkURLParser(@NotNull String urlString) {
            super(urlString);
            Matcher pageMatcher = PAGE_PATTERN.matcher(urlString);
            if (pageMatcher.matches()) {
                urlType = URLType.PAGE;
            } else {
                urlType = URLType.FILE;
            }
        }

        @Override
        @NotNull
        URLType getURLType() {
            return urlType;
        }
    }

    static class ImageURLParser extends URLParser {

        ImageURLParser(@NotNull String urlString) {
            super(urlString);
        }

        @Override
        @NotNull
        URLType getURLType() {
            return URLType.IMAGE;
        }

    }
}
