package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An application for recursively crawling a web page, downloading the elements it finds on the page such as images
 * and files.
 */
public class WebCrawler {

    private final URI uri;
    private final int maxDepth;
    private final WebElementRepository repository;

    WebCrawler(URI uri, int maxDepth, WebElementRepository repository) {
        this.uri = uri;
        this.maxDepth = maxDepth;
        this.repository = repository;
    }

    public static void main(String[] args) {
        WebCrawler crawler;
        try {
            verifySufficientArgCount(args);

            URI uri = getValidURI(args[0]);
            int maxDepth = getValidMaxDepth(args[1]);
            WebElementRepository repository = getValidDownloadRepository(args[2]);

            crawler = new WebCrawler(uri, maxDepth, repository);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        crawler.start();
    }

    // Starts the crawling
    void start() {
        // TODO
    }

    /**
     * The repository where this web crawler will store the web elements it locates.
     *
     * @return this web crawler's element repository.
     */
    @NotNull
    public WebElementRepository getRepository() {
        return repository;
    }

    private static void verifySufficientArgCount(@NotNull String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("A web address must be specified as the first argument.");
        } else if (args.length < 2) {
            throw new IllegalArgumentException("A maximum depth must be specified as the second argument.");
        } else if (args.length < 3) {
            throw new IllegalArgumentException("A local directory must be specified as the third argument.");
        }
    }

    @NotNull
    private static URI getValidURI(@NotNull String arg) {
        try {
            return URI.create(arg);
        } catch (IllegalArgumentException ignore) {
            throw new IllegalArgumentException("The web address (1st arg) is not formatted correctly.");
        }
    }

    private static int getValidMaxDepth(@NotNull String arg) {
        try {
            int maxDepth = Integer.parseInt(arg);
            if (maxDepth < 1) {
                throw new IllegalArgumentException();
            }
            return maxDepth;
        } catch (IllegalArgumentException ignore) {
            throw new IllegalArgumentException("The max depth (2nd arg) must be a natural number.");
        }
    }

    @NotNull
    private static WebElementRepository getValidDownloadRepository(@NotNull String arg) {
        try {
            Path localPath = Paths.get(arg);
            verifyValidDownloadRepository(localPath);
            DownloadRepository.INSTANCE.setDownloadLocation(localPath);
            return DownloadRepository.INSTANCE;
        } catch (InvalidPathException ignore) {
            throw new IllegalArgumentException("The local directory (3rd arg) must be a file path.");
        }
    }

    private static void verifyValidDownloadRepository(@NotNull Path path) {
        if (isNonDirectory(path)) {
            throw new IllegalArgumentException("The local directory (3rd arg) must be a directory or non-existent.");
        }
        if (!Files.isWritable(path)) {
            throw new IllegalArgumentException("The local directory (3rd arg) does not have write access.");
        }
    }

    private static boolean isNonDirectory(@NotNull Path path) {
        return Files.exists(path) && !Files.isDirectory(path);
    }
}
