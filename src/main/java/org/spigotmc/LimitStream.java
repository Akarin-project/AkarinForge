/*
 * Akarin Forge
 */
package org.spigotmc;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LimitStream
extends FilterInputStream {
    private final gh limit;

    public LimitStream(InputStream is2, gh limit) {
        super(is2);
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        this.limit.a(8);
        return super.read();
    }

    @Override
    public int read(byte[] b2) throws IOException {
        this.limit.a(b2.length * 8);
        return super.read(b2);
    }

    @Override
    public int read(byte[] b2, int off, int len) throws IOException {
        this.limit.a(len * 8);
        return super.read(b2, off, len);
    }
}

