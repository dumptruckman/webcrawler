package wood.poulos.webcrawler;

import java.net.URL;

/**
 * A abstract representation of a image on the web.
 */
public class WebImage extends AbstractWebElement implements WebElement {

    WebImage(URL url) {
        super(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "WebImage{url=" + getURL() + "}";
    }
}
