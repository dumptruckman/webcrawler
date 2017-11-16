package wood.poulos.webcrawler;

import org.jetbrains.annotations.NotNull;

class ImageURLParser extends URLParser {

    public ImageURLParser(@NotNull String urlString) {
        super(urlString);
    }

    @Override
    @NotNull
    URLType getURLType() {
        return URLType.IMAGE;
    }

}
