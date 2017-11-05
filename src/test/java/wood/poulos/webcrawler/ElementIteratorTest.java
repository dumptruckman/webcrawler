package wood.poulos.webcrawler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElementIteratorTest {

    @Test
    void testRemoveThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> new ElementIterator().remove());
    }

}