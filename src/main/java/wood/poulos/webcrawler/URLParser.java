package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;

abstract class URLParser {

    @NotNull
    static URLParser fromMatcher(@NotNull Matcher matcher) {
        matcher.group();
        String group1 = matcher.group(1);
        String group2 = matcher.group(2);
        String group3 = matcher.group(3);
        String group4 = matcher.group(4);


        if (!group1.isEmpty() || !group2.isEmpty()) {
            return new LinkURLParser(group1.isEmpty() ? group2 : group3);
        } else if (!group3.isEmpty() || !group4.isEmpty()) {
            return new LinkURLParser(group3.isEmpty() ? group4 : group4);
        } else {
            throw new IllegalArgumentException("Matcher has all empty capture groups.");
        }
    }

    @NotNull
    protected final URI uri;

    protected URLParser(@NotNull String urlString) {
        this.uri = URI.create(urlString);
    }

    @NotNull
    abstract URLType getURLType();


    @NotNull URL resolveURL(@NotNull URL parent) throws URISyntaxException, MalformedURLException {
        return parent.toURI().resolve(uri).toURL();
    }

    enum URLType {
        PAGE, FILE, IMAGE;
    }

}
