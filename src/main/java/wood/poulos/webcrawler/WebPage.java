package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * A abstract representation of a web page.
 */
public class WebPage implements WebElement {

    /**
     * Parses the concrete web page this WebPage represents and identifies all of its files, images, and links to other
     * pages.
     * <p>
     *     This must be done before using this class's various getters.
     * </p>
     */
    void crawl() {
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
     * Returns a new iterator for the images located on this page.
     * <p>
     *     An iterator is used here in order to allow lazy resolution of the images.
     * </p>
     *
     * @return a new iterator for the images located on this page.
     */
    @NotNull
    public Iterator<WebImage> getImages() {
        return null;
    }

    /**
     * Returns a new iterator for the files located on this page.
     * <p>
     *     An iterator is used here in order to allow lazy resolution of the files.
     * </p>
     *
     * @return a new iterator for the files located on this page.
     */
    @NotNull
    public Iterator<WebFile> getFiles() {
        return null;
    }

    /**
     * Returns a new iterator for the links to other web pages located on this page.
     * <p>
     *     An iterator is used here in order to allow lazy resolution of the pages.
     * </p>
     *
     * @return a new iterator for the links to other web pages located on this page.
     */
    @NotNull
    public Iterator<WebPage> getWebPages() {
        return null;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public URL getURL() {
        return null;
    }

    /** {@inheritDoc}
     * @param saveLocation*/
    @Override
    public void save(Path saveLocation) {
        throw new UnsupportedOperationException("page doesn't support save");
    }
}
