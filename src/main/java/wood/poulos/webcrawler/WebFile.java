package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * A abstract representation of a file on the web.
 */
public class WebFile implements WebElement {

    /** {@inheritDoc} */
    @NotNull
    @Override
    public URL getURL() {
        return null;
    }

    /** {@inheritDoc}
     * @param saveLocation*/
    @Override
    public void save(URL saveLocation) {

    }
}
