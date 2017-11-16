package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LinkURLParser extends URLParser {

    private final static Pattern PAGE_PATTERN = Pattern.compile("<\\s*a.*?href\\s*=\\s*(?:\"|')(.*html?|.*asp?|.*cgi?|.*php?|.*?)(?:\"|')*>(.*?)<\\s*?\\/\\s*?a\\s*?>");


    @NotNull
    private final URLParser.URLType urlType;

    public LinkURLParser(@NotNull String urlString) {
        super(urlString);
        Matcher pageMatcher = PAGE_PATTERN.matcher(urlString);
        if (pageMatcher.matches()) {
            urlType = URLType.PAGE;
        } else {
            urlType = URLType.FILE;
        }
    }

    @Override
    @NotNull
    URLParser.URLType getURLType() {
        return urlType;
    }
}
