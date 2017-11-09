package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wood.poulos.webcrawler.util.URLConverter;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

class DefaultLocalFileRepository implements LocalFileRepository {
    private final Path localPath;
    private final Set<WebElement> stagedElements = new HashSet<>();

    DefaultLocalFileRepository(Path localPath) {
        this.localPath = localPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Path getLocalDirectory() {
        return localPath;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
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
