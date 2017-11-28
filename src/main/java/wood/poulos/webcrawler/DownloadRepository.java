package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A singleton version of {@link LocalFileRepository}. Commits WebElements by downloading them to a directory specified
 * by {@link #setDownloadLocation(Path)}. If no download directory is specified, the files will be downloaded to the
 * current working directory.
 */
enum DownloadRepository implements WebElementRepository {
    /**
     * The singleton instance of this DownloadRepository.
     */
    INSTANCE;

    private LocalFileRepository localRepo = new LocalFileRepository(Paths.get("."));

    /**
     * Sets the download location for this repository.
     *
     * @param path The path where files will be downloaded to.
     */
    void setDownloadLocation(Path path) {
        LocalFileRepository newLocalRepo = new LocalFileRepository(path);
        copyCurrentElementsToOtherRepo(newLocalRepo);
        localRepo = newLocalRepo;
    }

    private void copyCurrentElementsToOtherRepo(@NotNull LocalFileRepository otherRepo) {
        for (WebElement e : getStagedElements()) {
            otherRepo.addElement(e);
        }
    }

    /**
     * Retrieves the path on the local disk for the given element.
     * <p>
     *     This path will be based on the url of the element and the download location of this repository.
     * </p>
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
