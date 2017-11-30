package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wood.poulos.webcrawler.util.URLDownloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Simple implementation of WebElement for the parts that are consistent across all web elements.
 */
abstract class AbstractWebElement implements WebElement {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    private final URL url;

    AbstractWebElement(URL url) {
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final URL getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     *
     * @param saveLocation
     */
    @Override
    public void save(Path saveLocation) {
        try {
            URLDownloader.downloadElement(getURL(), saveLocation);
        } catch (FileNotFoundException e) {
            logger.warn("Could not locate element {}", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getURL().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof WebElement)) {
            return false;
        }

        WebElement other = (WebElement) obj;
        return other.getURL().equals(this.getURL());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AbstractWebElement{" +
                "url=" + url +
                '}';
    }
}
