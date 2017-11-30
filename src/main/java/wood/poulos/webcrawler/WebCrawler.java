/*
 * MIT License
 *
 * Copyright (c) 2017 Jeremy Wood, Elijah Poulos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * An application for recursively crawling a web page, downloading the elements
 * it finds on the page such as images and files.
 */
public class WebCrawler {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    private final URI uri;
    private final int maxDepth;
    private final WebElementRepository repository;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Queue<Future<?>> crawlerQueue = new ConcurrentLinkedQueue<>();

    WebCrawler(URI uri, int maxDepth, WebElementRepository repository) {
        this.uri = uri;
        this.maxDepth = maxDepth;
        this.repository = repository;
    }

    /**
     * Runs the web crawler for the given arguments.
     * <p>
     * The first argument should be a valid URL to a website. The second
     * argument should be an integer greater than 0 indicating the recursive
     * page depth to crawl. The third argument should be the path to a local
     * directory to download web elements to.
     * </p>
     *
     * @param args the program arguments.
     * @throws MalformedURLException
     */
    public static void main(String[] args) {
        WebCrawler crawler;

        try {
            verifySufficientArgCount(args);

            // Transform program arguments into usable objects.
            URI uri = parseValidURL(args[0]);
            int maxDepth = parseValidMaxDepth(args[1]);
            WebElementRepository repository = parseValidDownloadRepository(args[2]);

            crawler = new WebCrawler(uri, maxDepth, repository);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return;
        }

        try {
            crawler.start();
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Crawls the web page specified by the URI passed into this WebCrawler's
     * constructor.
     */
    void start() throws MalformedURLException {
        WebPage page;
        try {
            page = WebElements.createWebPage(uri.toURL());
        } catch (IllegalArgumentException e) {
            throw new MalformedURLException("URL does not represent a web page URL.");
        }

        try {
            crawlPage(new CrawlerData(page, 0));
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return;
        }

        waitForCrawlsToFinish();

        repository.commit();
    }

    /**
     * Recursively crawls the {@link WebPage} contained within the given
     * crawlerData.
     *
     * @param crawlerData Contains the WebPage to crawl and the current depth
     *                    that page is at in the overall crawl.
     */
    void crawlPage(CrawlerData crawlerData) {
        WebPage page = crawlerData.page;
        int currentDepth = crawlerData.depth;

        if (currentDepth >= maxDepth) {
            logger.trace("{} is deeper than maxDepth", page.getURL());
            return;
        }

        try {
            logger.info("Crawling page at {}", page.getURL());
            page.crawl();
        } catch (IOException e) {
            if (currentDepth == 0) {
                throw new IllegalArgumentException("Could not connect to url: " + page.getURL());
            } else {
                logger.warn("Could not connect to url: {}", page.getURL());
            }
        }

        handlePageElements(page, currentDepth);
    }

    private void handlePageElements(@NotNull WebPage page, int currentDepth) {
        for (WebPage p : page.getWebPages()) {
            CrawlerData data = new CrawlerData(p, currentDepth + 1);
            crawlerQueue.add(executorService.submit(() -> crawlPage(data)));
        }
        for (WebImage i : page.getImages()) {
            repository.addElement(i);
        }
        for (WebFile f : page.getFiles()) {
            repository.addElement(f);
        }
    }

    private void waitForCrawlsToFinish() {
        logger.debug("Waiting for crawling to finish");
        Future<?> crawlerTask;
        while ((crawlerTask = crawlerQueue.poll()) != null) {
            try {
                crawlerTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        logger.info("Done crawling.");
        executorService.shutdown();
    }

    /**
     * The repository where this web crawler will store the web elements it
     * locates.
     *
     * @return this web crawler's element repository.
     */
    @NotNull
    public WebElementRepository getRepository() {
        return repository;
    }

    static void verifySufficientArgCount(@NotNull String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("A web address must be specified as the first argument.");
        } else if (args.length < 2) {
            throw new IllegalArgumentException("A maximum depth must be specified as the second argument.");
        } else if (args.length < 3) {
            throw new IllegalArgumentException("A local directory must be specified as the third argument.");
        }
    }

    @NotNull
    static URI parseValidURL(@NotNull String arg) {
        try {
            URI uri = URI.create(arg);
            try {
                WebElements.createWebPage(uri.toURL());
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException();
            }
            return uri;
        } catch (IllegalArgumentException ignore) {
            throw new IllegalArgumentException("The web address (1st arg) is not formatted correctly or does not represent a web page URL.");
        }
    }

    static int parseValidMaxDepth(@NotNull String arg) {
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
    static WebElementRepository parseValidDownloadRepository(@NotNull String arg) {
        try {
            Path localPath = Paths.get(arg);
            verifyValidDownloadRepository(localPath);
            DownloadRepository.INSTANCE.setDownloadLocation(localPath);
            return DownloadRepository.INSTANCE;
        } catch (InvalidPathException ignore) {
            throw new IllegalArgumentException("The local directory (3rd arg) must be a file path.");
        }
    }

    static void verifyValidDownloadRepository(@NotNull Path path) {
        if (isNonDirectory(path)) {
            throw new IllegalArgumentException("The local directory (3rd arg) must be a directory or non-existent.");
        }

        if ((path.toFile().exists() && !Files.isWritable(path))
                || (!path.toFile().exists() && path.getParent() != null && !Files.isWritable(path.getParent()))) {
            throw new IllegalArgumentException("The local directory (3rd arg) does not have write access.");
        }
    }

    private static boolean isNonDirectory(@NotNull Path path) {
        return Files.exists(path) && !Files.isDirectory(path);
    }

    /**
     * A simple tuple containing a {@link WebPage} and its current depth in a
     * crawl.
     */
    private static class CrawlerData {

        private final WebPage page;
        private final int depth;

        private CrawlerData(WebPage page, int depth) {
            this.page = page;
            this.depth = depth;
        }
    }
}
