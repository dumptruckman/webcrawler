package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import wood.poulos.webcrawler.util.ElementCopier;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * A abstract representation of a image on the web.
 */
public class WebImage implements WebElement {

    private final URL url;

    WebImage(URL url) {
        this.url = url;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public URL getURL() {
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
