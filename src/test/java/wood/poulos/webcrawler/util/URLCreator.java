package wood.poulos.webcrawler.util;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class URLCreator {

    public static URL create(@NotNull String url) {
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
