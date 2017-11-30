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

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class TestPrintStream extends PrintStream {

    private final ByteArrayOutputStream out;

    public TestPrintStream(@NotNull OutputStream out1, @NotNull ByteArrayOutputStream out2) {
        super(out1);
        this.out = out2;
    }

    @Override
    public void write(byte buf[], int off, int len) {
        try {
            super.write(buf, off, len);
            out.write(buf, off, len);
        } catch (Exception ignore) { }
    }

    public void reset() {
        out.reset();
    }

    @Override
    public String toString() {
        return out.toString();
    }

    public String getLastString() {
        String outString = out.toString();
        out.reset();

        String[] lines = outString.split(System.getProperty("line.separator"));

        for (int i = 0; i < lines.length; i++) {
            lines[i] = extractMessage(lines[i]);
        }

        return joinLines(lines);
    }

    private String extractMessage(String s) {
        String[] split;
        if (s.startsWith("[")) {
            // customized logger
            split = s.split(":", 2);
        } else {
            split = s.split("-", 2);
        }
        if (split.length != 2) {
            throw new IllegalStateException("The output does not appear to come from the logback logger.");
        }

        String message = split[1];
        return message.trim();
    }

    private String joinLines(String[] lines) {
        StringBuilder b = new StringBuilder();
        for (String line : lines) {
//            if (b.length() != 0) {
//                b.append(System.getProperty("line.separator"));
//            }
            b.append(line).append(System.getProperty("line.separator"));
        }
        return b.toString();
    }
}
