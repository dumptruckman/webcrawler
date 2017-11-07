package wood.poulos.webcrawler;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DownloadRepositoryTest {

    private DownloadRepository repo = DownloadRepository.INSTANCE;

    @Test
    void testGetStagedElementsEmptyByDefault() {
        Collection<WebElement> elements = new ArrayList<>();
        for (WebElement e : repo.getStagedElements()) {
            elements.add(e);
        }
        assertEquals(0, elements.size());
    }

}