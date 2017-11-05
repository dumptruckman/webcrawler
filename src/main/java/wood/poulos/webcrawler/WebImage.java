package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;

/**
 * A abstract representation of a image on the web.
 */
public class WebImage implements WebElement {

    /** {@inheritDoc} */
    @NotNull
    @Override
    public URL getURL() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void save(@NotNull URI uri) {

    }
}
