package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * A repository of web elements that downloads the elements as it
 */
enum DownloadRepository implements WebElementRepository {
    INSTANCE;

    /**
     * Sets the download location for this repository.
     *
     * @param path The path where files will be downloaded to.
     */
    void setDownloadLocation(Path path) {

    }

    /**
     * Retrieves the path on the local disk for the given element.
     *
     * @param element The element to get the path for.
     * @return The local path for the given element or null if the element is not contained within this repository.
     */
    @Nullable
    Path getLocalPathForElement(@NotNull WebElement element) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void addElement(@NotNull WebElement element) {

    }

    /** {@inheritDoc} */
    @Override
    public void removeElement(@NotNull WebElement element) {

    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public Iterable<WebElement> getElements() {
        return null;
    }
}
