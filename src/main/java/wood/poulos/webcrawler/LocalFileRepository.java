package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

interface LocalFileRepository extends WebElementRepository {

    /**
     * Sets the download location for this repository.
     *
     * @param path The path where files will be downloaded to.
     */
    void setLocalFileLocation(Path path);

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
