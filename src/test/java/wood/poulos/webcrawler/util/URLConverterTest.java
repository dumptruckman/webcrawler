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
package wood.poulos.webcrawler.util;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class URLConverterTest {

    @Test
    void testConvertToFilePathVariousValidURLs() throws Exception {
        assertEquals(Paths.get("www.google.com%2Fsearch"),
                URLConverter.convertToFilePath(URI.create("https://www.google.com/search?q=some+term").toURL()));
        assertEquals(Paths.get("localhost%3A8080%2Fjenkins%2Fheader.png"),
                URLConverter.convertToFilePath(URI.create("http://localhost:8080/jenkins/header.png").toURL()));
        assertEquals(Paths.get("c%3A%2Fwindows%2Fsystem32%2Fcmd.exe"),
                URLConverter.convertToFilePath(URI.create("file:c:/windows/system32/cmd.exe").toURL()));
    }

}