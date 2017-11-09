package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

interface LocalFileRepository extends WebElementRepository {

    /**
     * Returns the local directory files in this repository will be committed to.
     *
     * @return The local directory files in this repository will be committed to.
     */
    Path getLocalDirectory();

    /**
     * Retrieves the path on the local disk for the given element.
     *
     * @param element The element to get the path for.
     * @return The local path for the given element or null if the element is not contained within this repository.
     */
    @Nullable
    Path getLocalPathForElement(@NotNull WebElement element);

    /** {@inheritDoc} */
    @Override
    public void addElement(@NotNull WebElement element);

    /** {@inheritDoc} */
    @Override
    public void removeElement(@NotNull WebElement element);

    /** {@inheritDoc} */
    @Override
    @NotNull
    public Iterable<WebElement> getStagedElements();

    /** {@inheritDoc} */
    @Override
    public void commit();
}
