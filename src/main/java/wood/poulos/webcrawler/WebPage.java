package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A abstract representation of a web page.
 */
public class WebPage extends AbstractWebElement implements WebElement {

    /**
     * A RegEx pattern to find and differentiate links and images within HTML.
     */
    final static Pattern LINK_AND_IMAGE_PATTERN = Pattern.compile("(?:(?:a(?:\\s+?|\\s+?[\\S\\s]*?\\s+?)href\\s*=\\s*(?:\"([\\S\\s]+?)\"|'([\\S\\s]+?)')[\\S\\s]*?>)|(?:img(?:\\s+?|\\s+?[\\S\\s]*?\\s+?)src\\s*=\\s*(?:\"([\\S\\s]+?)\"|'([\\S\\s]+?)')[\\S\\s]*?>))[\\S\\s]*", Pattern.CASE_INSENSITIVE);

    /**
     * A RegEx pattern to determine if a URL path is a page or not.
     */
    final static Pattern PAGE_PATTERN = Pattern.compile("^((?!.*\\/.*#.*)|.*\\.html?|.*\\.aspx?|.*\\.jsp|.*\\.cgi|.*\\.php|[\\w\\d\\-~!$&'()*+,;=:@%/]+|.+\\/[^.]+)$", Pattern.CASE_INSENSITIVE);

    private boolean crawled = false;

    private Collection<WebPage> webPages;
    private Collection<WebFile> webFiles;
    private Collection<WebImage> webImages;

    /**
     * Creates a WebPage object for the given URL.
     *
     * @param url the URL to create the WebPage for.
     */
    WebPage(URL url) {
        super(url);
    }

    /**
     * Used internally to determine if the page has already been crawled.
     *
     * @return True if the page has already been crawled.
     */
    boolean isCrawled() {
        return crawled;
    }

    /**
     * Returns an unmodifiable collection of the images located on this page.
     *
     * @return an unmodifiable collection of the images located on this page.
     */
    @NotNull
    public Collection<WebImage> getImages() {
        return webImages;
    }

    /**
     * Returns an unmodifiable collection of the files located on this page.
     *
     * @return an unmodifiable collection of the files located on this page.
     */
    @NotNull
    public Collection<WebFile> getFiles() {
        return webFiles;
    }

    /**
     * Returns an unmodifiable collection of the links to other web pages located on this page.
     *
     * @return an unmodifiable collection of the links to other web pages located on this page.
     */
    @NotNull
    public Collection<WebPage> getWebPages() {
        return webPages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Path saveLocation) {
        throw new UnsupportedOperationException("page doesn't support save");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "WebPage{url=" + getURL() + "}";
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
        if (isCrawled()) {
            return;
        }

        System.out.println("Crawling " + getURL());

        // A triple for collecting the elements scraped from this page.
        ElementSets elementSets = new ElementSets();

        URLConnection conn = openConnection(getURL());
        Scanner input = getConfiguredScanner(conn.getInputStream());

        while (true) {
            if (hasSkippedUselessHTML(input)) {
                parseLinksAndImages(input, elementSets);
            } else {
                // EOF reached.
                break;
            }
        }

        // Unpack the triple into the appropriate collections.
        this.webPages = Collections.unmodifiableCollection(elementSets.webPages);
        this.webFiles = Collections.unmodifiableCollection(elementSets.webFiles);
        this.webImages = Collections.unmodifiableCollection(elementSets.webImages);

        crawled = true;
    }

    @NotNull
    private URLConnection openConnection(@NotNull URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.connect();
        return connection;
    }

    private Scanner getConfiguredScanner(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("<");
        return scanner;
    }

    /**
     * Skips all HTML that will not match the {@link #LINK_AND_IMAGE_PATTERN} which will result in the scanner's
     * next token being either non-existant or able to match the {@link #LINK_AND_IMAGE_PATTERN}.
     *
     * @param scanner The scanner that is scanning HTML data.
     * @return True if the next token should contain the {@link #LINK_AND_IMAGE_PATTERN} or false if scanner has reached
     * the end of the input.
     */
    private boolean hasSkippedUselessHTML(@NotNull Scanner scanner) {
        while (!scanner.hasNext(LINK_AND_IMAGE_PATTERN)) {
            if (scanner.hasNext()) {
                scanner.next();
            } else {
                return false;
            }
        }
        return true;
    }

    private void parseLinksAndImages(@NotNull Scanner input, @NotNull ElementSets elementSets) {
        while (input.hasNext(LINK_AND_IMAGE_PATTERN)) {
            String next = input.next(LINK_AND_IMAGE_PATTERN);

            Matcher matcher = LINK_AND_IMAGE_PATTERN.matcher(next);
            if (!matcher.matches()) {
                throw new IllegalStateException("Matcher failed to match");
            }

            WebElement element = getElementFromMatchedURL(matcher);
            System.out.println(element);
            if (element != null) {
                elementSets.addElementToAppropriateSet(element);
            }
        }
    }

    /**
     * Creates an appropriate WebElement object for the given matcher. The matcher should have already matched
     * the {@link #LINK_AND_IMAGE_PATTERN}.
     *
     * @param matcher the matcher which has found a potential URL that represents a WebElement to be gathered.
     * @return The WebElement that matches the URL matched by the given matcher or null if something is wrong with the
     * URL matched.
     */
    @Nullable
    private WebElement getElementFromMatchedURL(@NotNull Matcher matcher) {
        URLParser urlParser = URLParser.fromMatcher(matcher);
        try {
            URL resolvedURL = urlParser.resolveURL(getURL());
            System.out.println("Resolved: " + resolvedURL);
            switch (urlParser.getURLType()) {
                case PAGE:
                    return WebElements.createWebPage(resolvedURL);
                case FILE:
                    return WebElements.createWebFile(resolvedURL);
                case IMAGE:
                    return WebElements.createWebImage(resolvedURL);
            }
        } catch (URISyntaxException | MalformedURLException e) {
            System.out.println("Could not resolve url " + urlParser.uri + " against " + getURL());
            System.out.println(e.getMessage());
        }
        return null;
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

            String uri;
            if (group1 != null) {
                uri = group1.replaceAll("[\\r\\n\\t]", "");
            } else if (group2 != null) {
                uri = group2.replaceAll("[\\r\\n\\t]", "");
            } else if (group3 != null) {
                uri = group3.replaceAll("[\\r\\n\\t]", "");
            } else if (group4 != null) {
                uri = group4.replaceAll("[\\r\\n\\t]", "");
            } else {
                throw new IllegalArgumentException("Matcher has all empty capture groups.");
            }

            if (group1 != null || group2 != null) {
                return new LinkURLParser(uri);
            } else {
                return new ImageURLParser(uri);
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

    /**
     * A triple for collecting all of this web page's elements during a crawl.
     */
    private static class ElementSets {
        private final Set<WebPage> webPages = new HashSet<>();
        private final Set<WebFile> webFiles = new HashSet<>();
        private final Set<WebImage> webImages = new HashSet<>();

        private void addElementToAppropriateSet(@NotNull WebElement element) {
            if (element instanceof WebPage) {
                webPages.add((WebPage) element);
            } else if (element instanceof WebFile) {
                webFiles.add((WebFile) element);
            } else if (element instanceof WebImage) {
                webImages.add((WebImage) element);
            } else {
                throw new IllegalStateException("Unexpected WebElement type: " + element.getClass());
            }
        }
    }
}
