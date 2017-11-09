package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

class DefaultLocalFileRepository implements LocalFileRepository {
    private final Path localPath;

    public DefaultLocalFileRepository(Path localPath) {
        this.localPath = localPath;
    }

    /** {@inheritDoc} */
    @Override
    public Path getLocalDirectory() {
        return null;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public Path getLocalPathForElement(@NotNull WebElement element) {
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
    public Iterable<WebElement> getStagedElements() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void commit() {

    }
}
