package wood.poulos.webcrawler.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ElementCopier {
    public static void copyElement(URL from, URL to) throws IOException {
        System.out.println(to);
        URLConnection fromCon = from.openConnection();
        URLConnection toCon = to.openConnection();
        System.out.println(toCon);
        fromCon.connect();
        toCon.connect();
        try (InputStream inStream = fromCon.getInputStream();
             OutputStream outStream = toCon.getOutputStream()){
            IOUtils.copy(inStream, outStream);
        }
    }
}
