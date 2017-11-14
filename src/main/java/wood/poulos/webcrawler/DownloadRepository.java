package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A repository of web elements that downloads the elements as it
 */
enum DownloadRepository implements WebElementRepository {
    INSTANCE;

    LocalFileRepository localRepo = new LocalFileRepository(Paths.get("."));

    /**
     * Sets the download location for this repository.
     *
     * @param path The path where files will be downloaded to.
     */
    void setDownloadLocation(Path path) {
        LocalFileRepository newLocalRepo = new LocalFileRepository(path);
        for (WebElement e : getStagedElements()) {
            newLocalRepo.addElement(e);
        }
        localRepo = newLocalRepo;
    }

    /**
     * Retrieves the path on the local disk for the given element.
     *
     * @param element The element to get the path for.
     * @return The local path for the given element or null if the element is not contained within this repository.
     */
    @Nullable
    Path getLocalPathForElement(@NotNull WebElement element) {
        return localRepo.getLocalPathForElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addElement(@NotNull WebElement element) {
        localRepo.addElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeElement(@NotNull WebElement element) {
        localRepo.removeElement(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Iterable<WebElement> getStagedElements() {
        return localRepo.getStagedElements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        localRepo.commit();
    }

}
