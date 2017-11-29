package wood.poulos.webcrawler.util;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

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
}
