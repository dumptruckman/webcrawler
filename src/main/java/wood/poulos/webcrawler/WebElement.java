package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.nio.file.Path;

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
     * @param saveLocation The path to save the element to.
     */
    void save(Path saveLocation);

    /**
     * Determines if this WebElement is equivalent to the given other object.
     * <p>
     *     A WebElement is only ever equivalent to other WebElements and only when their URLs are equivalent.
     * </p>
     *
     * @param other The object to test equality against.
     * @return True iff the given other object is a WebElement and produces a URL from {@link #getURL()} equivalent to
     * the URL for this WebElement.
     */
    @Override
    boolean equals(@Nullable Object other);
}
