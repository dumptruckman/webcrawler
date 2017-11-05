package wood.poulos.webcrawler;

import java.util.Iterator;

class ElementIterator<E extends WebElement> implements Iterator<E> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
        return null;
    }
}
