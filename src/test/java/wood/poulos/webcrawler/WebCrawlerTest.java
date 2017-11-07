package wood.poulos.webcrawler;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermissions;

import static org.junit.jupiter.api.Assertions.*;

class WebCrawlerTest {

    @Test
    public void testVerifySufficientArgCount() {
        WebCrawler.verifySufficientArgCount(new String[] {"1", "2", "3"});
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {"1", "2"}));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {"1"}));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifySufficientArgCount(new String[] {}));
    }

    @Test
    public void testGetValidURIIllegalURIThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidURI("c:\\"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testGetValidURILegalURI() {
        assertTrue(WebCrawler.getValidURI("https://www.google.com") instanceof URI);
        assertTrue(WebCrawler.getValidURI("c:/") instanceof URI);
    }

    @Test
    public void testGetValidMaxDepthNonNumberThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("asdf"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("1b"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("--1"));
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("."));
    }

    @Test
    public void testGetValidMaxDepthNegativeNumberThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("-1"));
    }

    @Test
    public void testGetValidMaxDepthZeroThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidMaxDepth("0"));
    }

    @Test
    public void testGetValidMaxDepthValidNumbers() {
        assertEquals(1, WebCrawler.getValidMaxDepth("1"));
        assertEquals(2, WebCrawler.getValidMaxDepth("2"));
        assertEquals(10, WebCrawler.getValidMaxDepth("10"));
    }

    @Test
    public void testGetValidDownloadRepositoryInvalidPathThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.getValidDownloadRepository("/\\"));
    }

    @Test
    public void testGetValidDownloadRepositoryValidPath() {
        WebCrawler.getValidDownloadRepository(".");
    }

    @Test
    public void testVerifyValidDownloadRepositoryNonDirectoryThrowsIAE() throws Exception {
        Path p = Files.createTempFile(".", "tmp");
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifyValidDownloadRepository(p));
    }

    @Test
    public void testVerifyValidDownloadRepositoryNonWritableDirectoryThrowsIAE() throws Exception {
        Path p;
        if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
            p = Files.createTempDirectory("tmp", PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("r-xr-x---")));
        } else {
            FileAttribute<DosFileAttributes> attrib = new FileAttribute<DosFileAttributes>() {
                @Override
                public String name() {
                    return "readonly";
                }

                @Override
                public DosFileAttributes value() {
                    return new DosFileAttributes() {
                        @Override
                        public boolean isReadOnly() {
                            return true;
                        }

                        @Override
                        public boolean isHidden() {
                            return false;
                        }

                        @Override
                        public boolean isArchive() {
                            return false;
                        }

                        @Override
                        public boolean isSystem() {
                            return false;
                        }

                        @Override
                        public FileTime lastModifiedTime() {
                            return FileTime.fromMillis(0);
                        }

                        @Override
                        public FileTime lastAccessTime() {
                            return FileTime.fromMillis(0);
                        }

                        @Override
                        public FileTime creationTime() {
                            return FileTime.fromMillis(0);
                        }

                        @Override
                        public boolean isRegularFile() {
                            return false;
                        }

                        @Override
                        public boolean isDirectory() {
                            return true;
                        }

                        @Override
                        public boolean isSymbolicLink() {
                            return false;
                        }

                        @Override
                        public boolean isOther() {
                            return false;
                        }

                        @Override
                        public long size() {
                            return 1024;
                        }

                        @Override
                        public Object fileKey() {
                            return null;
                        }
                    };
                }
            };

            p = Files.createTempDirectory("tmp", attrib);
        }
        assertThrows(IllegalArgumentException.class, () -> WebCrawler.verifyValidDownloadRepository(p));
    }
}