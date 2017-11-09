package wood.poulos.webcrawler;

import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLCreator;

import java.net.URI;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class WebPageTest {

    @Test
    void testSaveThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> WebElements.createWebPage(URI.create("file://.").toURL()).save(Paths.get("./temp.html")));
    }

}