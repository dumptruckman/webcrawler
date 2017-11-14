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
    @NotNull
    Path getLocalDirectory();

    /**
     * Retrieves the path on the local disk for the given element.
     *
     * @param element The element to get the path for.
     * @return The local path for the given element or null if the element is not contained within this repository.
     */
    @Nullable
    Path getLocalPathForElement(@NotNull WebElement element);
}
