package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * A simple class containing factory methods for various {@link WebElement}s.
 */
class WebElements {

    /**
     * Creates a web page for the given url.
     *
     * @param url the url to create a web page for.
     * @return a web page for the given url.
     * @throws IllegalArgumentException If the given url does not point to a web page.
     */
    @NotNull
    static WebPage createWebPage(@NotNull URL url) throws IllegalArgumentException {
        String path = url.getPath();
        if (path.isEmpty() || WebPage.PAGE_PATTERN.matcher(path).matches()) {
            WebPage webPage = new WebPage(url);
            return webPage;
        }
        throw new IllegalArgumentException("URL does not point to a web page.");
    }

    /**
     * Creates a web image for the given url.
     *
     * @param url the url to create a web image for.
     * @return a web image for the given url.
     */
    @NotNull
    static WebImage createWebImage(@NotNull URL url) {
        WebImage webImage = new WebImage(url);
        return webImage;
    }

    /**
     * Creates a web file for the given url.
     *
     * @param url the url to create a web file for.
     * @return a web file for the given url.
     */
    @NotNull
    static WebFile createWebFile(@NotNull URL url) {
        return new WebFile(url);
    }
}
