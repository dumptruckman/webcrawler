package wood.poulos.webcrawler.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A utility for copying the contents of a URL to a file path.
 */
public class ElementCopier {

    /**
     * Copies the contents of a URL to a location on disc as given by a path.
     *
     * @param from The URL to copy from.
     * @param to The path to copy to.
     * @throws IOException If something goes wrong while trying to copy the contents.
     */
    public static void copyElement(URL from, Path to) throws IOException {
        URLConnection fromCon = from.openConnection();
        fromCon.connect();
        try (InputStream inStream = fromCon.getInputStream();
             OutputStream outStream = Files.newOutputStream(to)){
            IOUtils.copy(inStream, outStream);
        }
    }
}
