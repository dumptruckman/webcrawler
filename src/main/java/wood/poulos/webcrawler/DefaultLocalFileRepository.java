package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wood.poulos.webcrawler.util.URLConverter;

import java.nio.file.Path;

class DefaultLocalFileRepository implements LocalFileRepository {
    private final Path localPath;

    DefaultLocalFileRepository(Path localPath) {
        this.localPath = localPath;
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public Path getLocalDirectory() {
        return null;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public Path getLocalPathForElement(@NotNull WebElement element) {
        return getLocalDirectory().resolve(URLConverter.convertToFilePath(element.getURL()));
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
    public Iterable<WebElement> getStagedElements() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void commit() {

    }
}
