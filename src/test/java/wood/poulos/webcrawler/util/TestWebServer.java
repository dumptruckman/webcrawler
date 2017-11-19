package wood.poulos.webcrawler.util;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.SimpleWebServer;

import java.io.File;
import java.io.IOException;

public class TestWebServer {

    private final SimpleWebServer webServer;
    private final int port;

    public TestWebServer() {
        port = Integer.parseInt(System.getProperty("testWebServerPort", "8081"));
        webServer = new SimpleWebServer("localhost", port, new File("./testPages"), true);
    }

    public void start() throws IOException {
        webServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Test server running on http://localhost:" + port + "/");
    }

    public int getPort() {
        return port;
    }
}
