/*
 * MIT License
 *
 * Copyright (c) 2017 Jeremy Wood, Elijah Poulos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package wood.poulos.webcrawler.util;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.SimpleWebServer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class TestWebServer {

    public static final ReentrantLock lock = new ReentrantLock();

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

    public void stop() {
        webServer.stop();
    }

    public int getPort() {
        return port;
    }
}
