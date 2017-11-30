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

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * A simple class containing factory methods for various {@link WebElement}s.
 */
class WebElements {

    /**
     * Creates a web page for the given url.
     *
     * @param url the url to create a web page for.
     * @return a web page for the given url.
     * @throws IllegalArgumentException If the given url does not point to a web page.
     */
    @NotNull
    static WebPage createWebPage(@NotNull URL url) throws IllegalArgumentException {
        String path = url.getPath();
        if (path.isEmpty() || WebPage.PAGE_PATTERN.matcher(path).matches()) {
            WebPage webPage = new WebPage(url);
            return webPage;
        }
        throw new IllegalArgumentException("URL does not point to a web page.");
    }

    /**
     * Creates a web image for the given url.
     *
     * @param url the url to create a web image for.
     * @return a web image for the given url.
     */
    @NotNull
    static WebImage createWebImage(@NotNull URL url) {
        WebImage webImage = new WebImage(url);
        return webImage;
    }

    /**
     * Creates a web file for the given url.
     *
     * @param url the url to create a web file for.
     * @return a web file for the given url.
     */
    @NotNull
    static WebFile createWebFile(@NotNull URL url) {
        return new WebFile(url);
    }
}
