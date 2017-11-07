package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

/**
 * An abstract repository of web elements.
 *
 * TODO - This design may not work out in the end...
 */
public interface WebElementRepository {

    /**
     * Stages the given element for committing to the repository.
     *
     * @param element The element to stage.
     */
    void addElement(@NotNull WebElement element);

    /**
     * Unstages the given element so that it will not be committed to the repository.
     *
     * @param element The element to unstage.
     */
    void removeElement(@NotNull WebElement element);

    /**
     * Returns an iterable of all of the web elements staged for committing to the repository.
     *
     * @return an iterable of all of the web elements staged for committing to the repository.
     */
    @NotNull
    Iterable<WebElement> getStagedElements();

    /**
     * Synchronizes the contents of this repository
     */
    void commit();
}
