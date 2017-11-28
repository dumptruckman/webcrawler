package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

/**
 * An abstract repository of web elements.
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
     * Commits the elements in this repository. What this means will be implementation dependent, however, once
     * committed they will no longer be held in this repository.
     */
    void commit();
}
