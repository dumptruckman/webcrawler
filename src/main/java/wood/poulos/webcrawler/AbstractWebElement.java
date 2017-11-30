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
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wood.poulos.webcrawler.util.URLDownloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Simple implementation of WebElement for the parts that are consistent across
 * all web elements.
 */
abstract class AbstractWebElement implements WebElement {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    private final URL url;

    AbstractWebElement(URL url) {
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final URL getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     *
     * @param saveLocation
     */
    @Override
    public void save(Path saveLocation) {
        try {
            URLDownloader.downloadElement(getURL(), saveLocation);
        } catch (FileNotFoundException e) {
            logger.warn("Could not locate element {}", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getURL().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof WebElement)) {
            return false;
        }

        WebElement other = (WebElement) obj;
        return other.getURL().equals(this.getURL());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AbstractWebElement{" +
                "url=" + url +
                '}';
    }
}
