package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wood.poulos.webcrawler.util.URLDownloader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Simple implementation of WebElement for the parts that are consistent across all web elements.
 */
abstract class AbstractWebElement implements WebElement {

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
            URLDownloader.copyElement(getURL(), saveLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return getURL().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof WebElement))
            return false;
        WebElement other = (WebElement) obj;
        return other.getURL().equals(this.getURL());
    }

    @Override
    public String toString() {
        return "AbstractWebElement{" +
                "url=" + url +
                '}';
    }
}
