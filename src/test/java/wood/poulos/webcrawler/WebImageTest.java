package wood.poulos.webcrawler;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLCreator;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class WebImageTest {

    @Test
    void testSaveTestFileToTempFileProducesIdenticalImage() throws Exception{
        WebElement image = WebElements.createWebImage(URLCreator.create("file:./testPages/images/image1.png"));
        File originalFile = new File("./testPages/images/image1.png");
        File tempFile = Files.createTempFile("image1", ".png").toFile();
        image.save(tempFile.toURI().toURL());
        assertTrue(FileUtils.contentEqualsIgnoreEOL(originalFile, tempFile, null));
    }

}