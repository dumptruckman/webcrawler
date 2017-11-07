package wood.poulos.webcrawler;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class WebPageTest {

    @Test
    void testSaveThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> WebElements.createWebPage(URI.create("file://.").toURL()).save(URI.create("")));
    }

}