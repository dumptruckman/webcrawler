package wood.poulos.webcrawler.util;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class FileDownloadVerifier {

    public static boolean isFileDownloadedSuccessfully(@NotNull URL url, @NotNull Path tempDir) throws IOException {
        File original = new File("./testPages/text_files/text_file_1.txt");
        File downloaded = tempDir.resolve(URLConverter.convertToFilePath(url)).toFile();
        return FileUtils.contentEquals(original, downloaded);
    }
}
