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
import wood.poulos.webcrawler.util.URLConverter;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * A repository of {@link WebElement}s that will commit elements by downloading them to the path specified in the
 * constructor {@link #LocalFileRepository(Path)}.
 */
public class LocalFileRepository implements WebElementRepository {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    private final Path localPath;
    private final Set<WebElement> stagedElements = new HashSet<>();

    /**
     * Constructs a LocalFileRepository with the given localPath download location.
     *
     * @param localPath The path to download web elements to when committed.
     */
    LocalFileRepository(Path localPath) {
        this.localPath = localPath;
    }

    /**
     * Returns the local directory files in this repository will be committed to.
     *
     * @return The local directory files in this repository will be committed to.
     */
    @NotNull
    public Path getLocalDirectory() {
        return localPath;
    }

    /**
     * Retrieves the path on the local disk for the given element.
     * <p>
     *     This path will be based on the url of the element and the download location of this repository.
     * </p>
     *
     * @param element The element to get the path for.
     * @return The local path for the given element or null if the element is not contained within this repository.
     */
    @Nullable
    public Path getLocalPathForElement(@NotNull WebElement element) {
        return getLocalDirectory().resolve(URLConverter.convertToFilePath(element.getURL()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addElement(@NotNull WebElement element) {
        stagedElements.add(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeElement(@NotNull WebElement element) {
        stagedElements.remove(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Iterable<WebElement> getStagedElements() {
        return stagedElements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        logger.info("Begin downloading...");
        stagedElements.parallelStream()
                .filter(e -> !(e instanceof WebPage))
                .forEach(e -> {
                    logger.trace("Saving {} to {}", e, getLocalPathForElement(e));
                    e.save(getLocalPathForElement(e));
                });
        stagedElements.clear();
        logger.info("Done downloading.");
    }
}
