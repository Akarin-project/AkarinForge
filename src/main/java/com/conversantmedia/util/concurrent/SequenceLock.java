/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.Condition;
import com.conversantmedia.util.concurrent.ContendedAtomicLong;
import com.conversantmedia.util.concurrent.OptimisticLock;
import java.util.concurrent.TimeUnit;

public class SequenceLock
implements OptimisticLock {
    final ContendedAtomicLong sequence = new ContendedAtomicLong(2);

    @Override
    public long readLock() {
        return this.sequence.get();
    }

    @Override
    public boolean readLockHeld(long lockToken) {
        return this.sequence.get() == lockToken && (lockToken & 1) == 0;
    }

    @Override
    public long writeLock() {
        int spin = 0;
        long sequence;
        while (((sequence = this.sequence.get()) & 1) != 0 || !this.sequence.compareAndSet(sequence, sequence + 1)) {
            spin = Condition.progressiveYield(spin);
        }
        return sequence;
    }

    @Override
    public void unlock(long sequence) {
        this.sequence.set(sequence + 2);
    }

    @Override
    public long tryWriteLock() {
        long sequence = this.sequence.get();
        if ((sequence & 1) == 0 && this.sequence.compareAndSet(sequence, sequence + 1)) {
            return sequence;
        }
        return 0;
    }

    @Override
    public long tryWriteLock(long time, TimeUnit unit) throws InterruptedException {
        long toNanos = System.nanoTime() + unit.toNanos(time);
        long sequence = this.tryWriteLock();
        int spin = 0;
        while (sequence == 0 && toNanos - System.nanoTime() > 0) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            Condition.progressiveYield(spin);
            sequence = this.tryWriteLock();
        }
        return sequence;
    }

    @Override
    public long tryWriteLockInterruptibly() throws InterruptedException {
        long sequence = this.tryWriteLock();
        int spin = 0;
        while (sequence == 0) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            Condition.progressiveYield(spin);
            sequence = this.tryWriteLock();
        }
        return sequence;
    }
}

