package wood.poulos.webcrawler;

import java.net.URL;

/**
 * A abstract representation of a file on the web.
 */
public class WebFile extends AbstractWebElement implements WebElement {
    public WebFile(URL url) {
        super(url);
    }

    @Override
    public String toString() {
        return "WebFile{url=" + getURL() + "}";
    }
}
