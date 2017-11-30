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

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A singleton version of {@link LocalFileRepository}. Commits WebElements by
 * downloading them to a directory specified by
 * {@link #setDownloadLocation(Path)}. If no download directory is specified,
 * the files will be downloaded to the current working directory.
 */
enum DownloadRepository implements WebElementRepository {
    /**
     * The singleton instance of this DownloadRepository.
     */
    INSTANCE;

    private LocalFileRepository localRepo = new LocalFileRepository(Paths.get("."));

    /**
     * Sets the download location for this repository.
     *
     * @param path The path where files will be downloaded to.
     */
    void setDownloadLocation(Path path) {
        LocalFileRepository newLocalRepo = new LocalFileRepository(path);
        copyCurrentElementsToOtherRepo(newLocalRepo);
        localRepo = newLocalRepo;
    }

    private void copyCurrentElementsToOtherRepo(@NotNull LocalFileRepository otherRepo) {
        for (WebElement e : getStagedElements()) {
            otherRepo.addElement(e);
        }
    }

    /**
     * Retrieves the path on the local disk for the given element.
     * <p>
     * This path will be based on the url of the element and the download
     * location of this repository.
     * </p>
     *
     * @param element The element to get the path for.
     * @return The local path for the given element or null if the element is
     * not contained within this repository.
     */
    @Nullable
    Path getLocalPathForElement(@NotNull WebElement element) {
        return localRepo.getLocalPathForElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addElement(@NotNull WebElement element) {
        localRepo.addElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeElement(@NotNull WebElement element) {
        localRepo.removeElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Iterable<WebElement> getStagedElements() {
        return localRepo.getStagedElements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        localRepo.commit();
    }

}
