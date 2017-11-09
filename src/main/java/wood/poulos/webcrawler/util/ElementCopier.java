package wood.poulos.webcrawler.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class ElementCopier {
    public static void copyElement(URL from, Path to) throws IOException {
        URLConnection fromCon = from.openConnection();
        fromCon.connect();
        try (InputStream inStream = fromCon.getInputStream();
             OutputStream outStream = Files.newOutputStream(to)){
            IOUtils.copy(inStream, outStream);
        }
    }
}
