/*
 * MIT License
 *
 * Copyright (c) 2017 Jeremy Wood, Elijah Poulos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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