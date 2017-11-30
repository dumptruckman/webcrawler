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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLConverter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static wood.poulos.webcrawler.LocalFileRepositoryTest.RELIABLE_IMAGE;
import static wood.poulos.webcrawler.LocalFileRepositoryTest.getStagedElements;

class DownloadRepositoryTest {

    DownloadRepository repo = DownloadRepository.INSTANCE;

    @BeforeEach
    public void setup() {
        repo.setDownloadLocation(Paths.get("."));
        Collection<WebElement> elements = getStagedElements(repo);
        for (WebElement e : elements) {
            repo.removeElement(e);
        }
    }

    @Test
    void testSetDownloadLocationPreservesAddedElements() {
        Collection<WebElement> elements = getStagedElements(repo);
        assertTrue(elements.isEmpty());
        repo.setDownloadLocation(Paths.get("./tmp"));
        elements = getStagedElements(repo);
        assertTrue(elements.isEmpty());

        repo.addElement(RELIABLE_IMAGE);
        elements = getStagedElements(repo);
        assertTrue(elements.contains(RELIABLE_IMAGE));
        repo.setDownloadLocation(Paths.get("./tmp2"));
        elements = getStagedElements(repo);
        assertTrue(elements.contains(RELIABLE_IMAGE));
    }

    @Test
    void testGetLocalPathForElementWhenDownloadLocationSetToCurrentDirectory() throws Exception {
        repo.setDownloadLocation(Paths.get("."));
        WebElement element = RELIABLE_IMAGE;
        repo.addElement(element);
        Path currentDirectory = Paths.get(".");
        Path expectedPath = currentDirectory.resolve(URLConverter.convertToFilePath(element.getURL()));
        assertEquals(expectedPath, repo.getLocalPathForElement(element));
    }

    @Test
    void testGetLocalPathForElementWhenDownloadLocationChanged() throws Exception {
        repo.setDownloadLocation(Paths.get("."));
        WebElement element = RELIABLE_IMAGE;
        repo.addElement(element);
        Path currentDirectory = Paths.get(".");
        Path expectedPath = currentDirectory.resolve(URLConverter.convertToFilePath(element.getURL()));
        assertEquals(expectedPath, repo.getLocalPathForElement(element));

        repo.setDownloadLocation(Paths.get("./tmp"));
        Path tempDirectory = Paths.get("./tmp");
        expectedPath = tempDirectory.resolve(URLConverter.convertToFilePath(element.getURL()));
        assertEquals(expectedPath, repo.getLocalPathForElement(element));
    }

}