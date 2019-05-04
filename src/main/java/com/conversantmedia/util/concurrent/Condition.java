/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

interface Condition {
    public static final long PARK_TIMEOUT = 50;
    public static final int MAX_PROG_YIELD = 2000;

    public boolean test();

    public void awaitNanos(long var1) throws InterruptedException;

    public void await() throws InterruptedException;

    public void signal();

    /*
     * Enabled aggressive block sorting
     */
    default public static int progressiveYield(int n2) {
        if (n2 <= 500) {
            Condition.onSpinWait();
            return n2 + 1;
        }
        if (n2 < 1000) {
            if ((n2 & 7) == 0) {
                LockSupport.parkNanos(50);
                return n2 + 1;
            }
            Condition.onSpinWait();
            return n2 + 1;
        }
        if (n2 >= 2000) {
            Thread.yield();
            return n2;
        }
        if ((n2 & 3) == 0) {
            Thread.yield();
            return n2 + 1;
        }
        Condition.onSpinWait();
        return n2 + 1;
    }

    default public static void onSpinWait() {
    }

    default public static boolean waitStatus(long timeout, TimeUnit unit, Condition condition) throws InterruptedException {
        long timeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit);
        long expireTime = System.nanoTime() + timeoutNanos;
        while (condition.test()) {
            long now = System.nanoTime();
            if (now > expireTime) {
                return false;
            }
            condition.awaitNanos(expireTime - now - 50);
        }
        return true;
    }
}

