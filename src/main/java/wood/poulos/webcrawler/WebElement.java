package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;

/**
 * An abstract representation of some element of the web.
 */
public interface WebElement {

    /**
     * Returns the URL of this web element.
     *
     * @return the URL of this web element.
     */
    @NotNull
    URL getURL();

    /**
     * Saves this web element to the given path.
     * <p>
     *     The saved format of the element should exactly represent the element as served on the web.
     * </p>
     *
     * @param uri The resource to save the element to.
     */
    void save(@NotNull URI uri);
}
