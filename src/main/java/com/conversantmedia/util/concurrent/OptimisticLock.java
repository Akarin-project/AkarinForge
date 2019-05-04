/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import java.util.concurrent.TimeUnit;

public interface OptimisticLock {
    public long readLock();

    public boolean readLockHeld(long var1);

    public long writeLock();

    public long tryWriteLockInterruptibly() throws InterruptedException;

    public long tryWriteLock();

    public long tryWriteLock(long var1, TimeUnit var3) throws InterruptedException;

    public void unlock(long var1);
}

