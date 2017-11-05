package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

/**
 * A repository of web elements.
 *
 * TODO - This design may not work out in the end...
 */
public interface WebElementRepository {

    /**
     * Adds the given element to this repository.
     *
     * @param element The element to add to the repository.
     */
    void addElement(@NotNull WebElement element);

    /**
     * Removes the given element from this repository.
     *
     * @param element The element to remove from the repository.
     */
    void removeElement(@NotNull WebElement element);

    /**
     * Returns an iterable of all of the web elements contained in this repository.
     *
     * @return An iterable of all of the web elements contained in this repository.
     */
    @NotNull
    Iterable<WebElement> getElements();
}
