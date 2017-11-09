package wood.poulos.webcrawler;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLCreator;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WebFileTest {

    @Test
    void testSaveTestFileToTempFileProducesIdenticalFile() throws Exception{
        WebElement file = WebElements.createWebImage(URLCreator.create("file:./testPages/text_files/text_file_1.txt"));
        File originalFile = new File("./testPages/text_files/text_file_1.txt");
        File tempFile = Files.createTempFile("text_file_1", ".txt").toFile();
        file.save(tempFile.toPath());
        assertTrue(FileUtils.contentEqualsIgnoreEOL(originalFile, tempFile, null));
    }

}