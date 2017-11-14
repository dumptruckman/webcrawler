package wood.poulos.webcrawler;

import java.net.URL;

/**
 * A abstract representation of a image on the web.
 */
public class WebImage extends AbstractWebElement implements WebElement {

    public WebImage(URL url) {
        super(url);
    }
}
