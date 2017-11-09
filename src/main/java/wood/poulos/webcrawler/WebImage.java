package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import wood.poulos.webcrawler.util.ElementCopier;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * A abstract representation of a image on the web.
 */
public class WebImage extends AbstractWebElement implements WebElement {

    public WebImage(URL url) {
        super(url);
    }
}
