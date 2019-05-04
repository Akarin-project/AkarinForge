/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.ContendedAtomicLong;
import java.util.concurrent.atomic.AtomicIntegerArray;

final class ContendedAtomicInteger {
    private static final int CACHE_LINE_INTS = ContendedAtomicLong.CACHE_LINE / 4;
    private final AtomicIntegerArray contendedArray = new AtomicIntegerArray(2 * CACHE_LINE_INTS);

    public ContendedAtomicInteger(int init) {
        this.set(init);
    }

    public int get() {
        return this.contendedArray.get(CACHE_LINE_INTS);
    }

    public void set(int i2) {
        this.contendedArray.set(CACHE_LINE_INTS, i2);
    }

    public boolean compareAndSet(int expect, int i2) {
        return this.contendedArray.compareAndSet(CACHE_LINE_INTS, expect, i2);
    }

    public String toString() {
        return Integer.toString(this.get());
    }
}

