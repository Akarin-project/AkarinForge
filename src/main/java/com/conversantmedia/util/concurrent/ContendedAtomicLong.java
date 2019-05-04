/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import java.util.concurrent.atomic.AtomicLongArray;

final class ContendedAtomicLong {
    static final int CACHE_LINE = Integer.getInteger("Intel.CacheLineSize", 64);
    private static final int CACHE_LINE_LONGS = CACHE_LINE / 8;
    private final AtomicLongArray contendedArray = new AtomicLongArray(2 * CACHE_LINE_LONGS);

    ContendedAtomicLong(long init) {
        this.set(init);
    }

    void set(long l2) {
        this.contendedArray.set(CACHE_LINE_LONGS, l2);
    }

    long get() {
        return this.contendedArray.get(CACHE_LINE_LONGS);
    }

    public String toString() {
        return Long.toString(this.get());
    }

    public boolean compareAndSet(long expect, long l2) {
        return this.contendedArray.compareAndSet(CACHE_LINE_LONGS, expect, l2);
    }
}

