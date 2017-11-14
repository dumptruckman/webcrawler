package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wood.poulos.webcrawler.util.URLConverter;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class LocalFileRepository implements WebElementRepository {
    private final Path localPath;
    private final Set<WebElement> stagedElements = new HashSet<>();

    LocalFileRepository(Path localPath) {
        this.localPath = localPath;
    }

    /**
     * Returns the local directory files in this repository will be committed to.
     *
     * @return The local directory files in this repository will be committed to.
     */
    @NotNull
    public Path getLocalDirectory() {
        return localPath;
    }

    /**
     * Retrieves the path on the local disk for the given element.
     *
     * @param element The element to get the path for.
     * @return The local path for the given element or null if the element is not contained within this repository.
     */
    @Nullable
    public Path getLocalPathForElement(@NotNull WebElement element) {
        return getLocalDirectory().resolve(URLConverter.convertToFilePath(element.getURL()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addElement(@NotNull WebElement element) {
        stagedElements.add(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeElement(@NotNull WebElement element) {
        stagedElements.remove(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Iterable<WebElement> getStagedElements() {
        return stagedElements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {

    }
}
