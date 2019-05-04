/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

final class Capacity {
    public static final int MAX_POWER2 = 1073741824;

    Capacity() {
    }

    public static int getCapacity(int capacity) {
        int c2;
        if (capacity >= 1073741824) {
            c2 = 1073741824;
        } else {
            for (c2 = 1; c2 < capacity; c2 <<= 1) {
            }
        }
        if (Capacity.isPowerOf2(c2)) {
            return c2;
        }
        throw new RuntimeException("Capacity is not a power of 2.");
    }

    private static final boolean isPowerOf2(int p2) {
        return (p2 & p2 - 1) == 0;
    }
}

