package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

public class URLParserTest {

    private WebPage.URLParser absoluteURLParser;
    private WebPage.URLParser relativeURLParser;

    @BeforeEach
    void setUp() {
        absoluteURLParser = getParser("<a href=\"http://www.google.com/\">");
    }

    @NotNull
    private WebPage.URLParser getParser(@NotNull String url) {
        Matcher m = WebPage.LINK_AND_IMAGE_PATTERN.matcher(url);
        assertTrue(m.matches());
        return WebPage.URLParser.fromMatcher(m);
    }

    @Test
    void testFromMatcherOnVariousValidURLMatches() {
        WebPage.URLParser parser;

        parser = getParser("<a href=\"http://www.google.com/\">");
        assertTrue(parser instanceof WebPage.LinkURLParser);
        parser = getParser("<a href=\"http://www.google.com/index.html\">");
        assertTrue(parser instanceof WebPage.LinkURLParser);
        parser = getParser("<img test='' src=\"http://www.google.com/logo.png\" target='hello'>");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("<img     src = \"/images/image1.jpg\"    >");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("<a href=\"/\">");
        assertTrue(parser instanceof WebPage.LinkURLParser);
        parser = getParser("<a href=\"../\">");
        assertTrue(parser instanceof WebPage.LinkURLParser);
        parser = getParser("<img src=\"image1.gif\">");
        assertTrue(parser instanceof WebPage.ImageURLParser);
    }
}
