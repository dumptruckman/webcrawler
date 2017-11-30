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

import java.net.URL;
import java.nio.file.Path;

/**
 * An abstract representation of some element of the web.
 */
public interface WebElement {

    /**
     * Returns the URL of this web element.
     *
     * @return the URL of this web element.
     */
    @NotNull
    URL getURL();

    /**
     * Saves this web element to the given path.
     * <p>
     * The saved format of the element should exactly represent the element as
     * served on the web.
     * </p>
     *
     * @param saveLocation The path to save the element to.
     */
    void save(Path saveLocation);

    /**
     * Determines if this WebElement is equivalent to the given other object.
     * <p>
     * A WebElement is only ever equivalent to other WebElements and only when
     * their URLs are equivalent.
     * </p>
     *
     * @param other The object to test equality against.
     * @return True iff the given other object is a WebElement and produces a
     * URL from {@link #getURL()} equivalent to the URL for this WebElement.
     */
    @Override
    boolean equals(@Nullable Object other);
}
