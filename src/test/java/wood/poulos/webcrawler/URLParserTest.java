package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wood.poulos.webcrawler.util.URLCreator;

import java.net.URL;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

public class URLParserTest {

    @NotNull
    private WebPage.URLParser getParser(@NotNull String url) {
        Matcher m = WebPage.LINK_AND_IMAGE_PATTERN.matcher(url);
        assertTrue(m.matches());
        return WebPage.URLParser.fromMatcher(m);
    }

    @Test
    void testFromMatcherOnVariousValidURLMatches() {
        WebPage.URLParser parser;

        parser = getParser("a href=\"http://www.google.com/\">");
        assertTrue(parser instanceof WebPage.LinkURLParser);
        parser = getParser("a href=\"http://www.google.com/index.html\">");
        assertTrue(parser instanceof WebPage.LinkURLParser);
        parser = getParser("img test='' src=\"http://www.google.com/logo.png\" target='hello'>");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("img     src = \"/images/image1.jpg\"    >");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("a href=\"/\">");
        assertTrue(parser instanceof WebPage.LinkURLParser);
        parser = getParser("a href=\"../\">");
        assertTrue(parser instanceof WebPage.LinkURLParser);
        parser = getParser("img src=\"image1.gif\">");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("img src=\"image1.gif\">asdfasdf");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("img src=\"image1.gif\">\nsdfasdfa");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("img\nsrc=\"image1.gif\">");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("img\ntest='' \n src=\"http://www.google.com/logo.png\" target='hello'>");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("img src=\"image\n1.gif\">");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("img src=\"image\t\t1.gif\">");
        assertTrue(parser instanceof WebPage.ImageURLParser);
        parser = getParser("IMG SRC=\"image\t\t1.gif\">");
        assertTrue(parser instanceof WebPage.ImageURLParser);
    }

    @Test
    void testResolveURLCorrectlyResolvesRelativeURLs() throws Exception {
        WebPage.URLParser parser;

        parser = getParser("img     src = \"/images/image1.jpg\"    >");
        assertEquals(URLCreator.create("http://www.test.com/images/image1.jpg"),
                parser.resolveURL(URLCreator.create("http://www.test.com/")));
        assertEquals(URLCreator.create("http://www.test.com/images/image1.jpg"),
                parser.resolveURL(URLCreator.create("http://www.test.com")));

        parser = getParser("img     src = \"../../../images/image1.jpg\"    >");
        assertEquals(URLCreator.create("http://www.test.com/images/image1.jpg"),
                parser.resolveURL(URLCreator.create("http://www.test.com/churches/testing/woo")));
        assertEquals(URLCreator.create("http://www.test.com/images/image1.jpg"),
                parser.resolveURL(URLCreator.create("http://www.test.com/churches/testing/woo/index.html")));
    }

    @Test
    void testGetURLTypeReturnedExpectedTypeForSampleURLs() {
        WebPage.URLParser parser;

        parser = getParser("img src=\"/images/image1.jpg\">");
        assertEquals(WebPage.URLParser.URLType.IMAGE, parser.getURLType());

        parser = getParser("a href=\"/images/test.txt\">");
        assertEquals(WebPage.URLParser.URLType.FILE, parser.getURLType());

        parser = getParser("a href=\"/images\">");
        assertEquals(WebPage.URLParser.URLType.PAGE, parser.getURLType());

        parser = getParser("a href=\"/images/index.htm\">");
        assertEquals(WebPage.URLParser.URLType.PAGE, parser.getURLType());

        parser = getParser("a href=\"/images/\">");
        assertEquals(WebPage.URLParser.URLType.PAGE, parser.getURLType());

        parser = getParser("a href=\"https://www.google.com\">");
        assertEquals(WebPage.URLParser.URLType.PAGE, parser.getURLType());
    }
}
