package wood.poulos.webcrawler;

import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLCreator;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class WebElementTest {

    @Test
    void testEqualsAnyWebElementsWithSameURLAreEqual() {
        WebElement e1 = WebElements.createWebPage(URLCreator.create("http://test.xyz"));
        WebElement e2 = WebElements.createWebPage(URLCreator.create("http://test.xyz"));
        WebElement e3 = WebElements.createWebImage(URLCreator.create("http://test.xyz"));
        WebElement e4 = WebElements.createWebFile(URLCreator.create("http://test.xyz"));

        assertEquals(e1, e1);
        assertEquals(e1, e2);
        assertEquals(e1, e3);
        assertEquals(e1, e4);
        assertEquals(e2, e2);
        assertEquals(e2, e3);
        assertEquals(e2, e4);
        assertEquals(e3, e3);
        assertEquals(e3, e4);
        assertEquals(e4, e4);
    }

    @Test
    void testEqualsAnyWebElementsWithDifferentURLAreNotEqual() {
        WebElement e1 = WebElements.createWebPage(URLCreator.create("http://test.xyz"));
        WebElement e2 = WebElements.createWebPage(URLCreator.create("http://test.com"));
        WebElement e3 = WebElements.createWebImage(URLCreator.create("http://test.xyz/image.jpg"));
        WebElement e4 = WebElements.createWebFile(URLCreator.create("http://test.xyz/textfile.txt"));

        assertNotEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
        assertNotEquals(e2, e3);
        assertNotEquals(e2, e4);
        assertNotEquals(e3, e4);
    }
}