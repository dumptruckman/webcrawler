package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import wood.poulos.webcrawler.util.ElementCopier;

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

    /** {@inheritDoc} */
    @NotNull
    @Override
    public final URL getURL() {
        return url;
    }

    /** {@inheritDoc}
     * @param saveLocation*/
    @Override
    public void save(Path saveLocation) {
        try {
            ElementCopier.copyElement(getURL(),saveLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
