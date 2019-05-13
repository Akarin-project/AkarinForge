/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

public class LongHash {
    public static long toLong(int msw, int lsw) {
        return ((long)msw << 32) + (long)lsw - Integer.MIN_VALUE;
    }

    public static int msw(long l2) {
        return (int)(l2 >> 32);
    }

    public static int lsw(long l2) {
        return (int)(l2 & -1) + Integer.MIN_VALUE;
    }
}

