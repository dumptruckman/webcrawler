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