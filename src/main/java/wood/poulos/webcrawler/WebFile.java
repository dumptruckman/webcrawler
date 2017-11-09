package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import wood.poulos.webcrawler.util.ElementCopier;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * A abstract representation of a file on the web.
 */
public class WebFile extends AbstractWebElement implements WebElement {
    public WebFile(URL url) {
        super(url);
    }
}
