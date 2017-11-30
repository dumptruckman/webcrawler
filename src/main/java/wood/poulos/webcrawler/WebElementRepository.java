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

/**
 * An abstract repository of web elements.
 */
public interface WebElementRepository {

    /**
     * Stages the given element for committing to the repository.
     *
     * @param element The element to stage.
     */
    void addElement(@NotNull WebElement element);

    /**
     * Unstages the given element so that it will not be committed to the repository.
     *
     * @param element The element to unstage.
     */
    void removeElement(@NotNull WebElement element);

    /**
     * Returns an iterable of all of the web elements staged for committing to the repository.
     *
     * @return an iterable of all of the web elements staged for committing to the repository.
     */
    @NotNull
    Iterable<WebElement> getStagedElements();

    /**
     * Commits the elements in this repository. What this means will be implementation dependent, however, once
     * committed they will no longer be held in this repository.
     */
    void commit();
}
