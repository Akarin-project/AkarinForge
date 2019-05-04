/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.Condition;
import com.conversantmedia.util.concurrent.ContendedAtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

public abstract class AbstractWaitingCondition
implements Condition {
    private static final int CACHE_LINE_REFS = ContendedAtomicLong.CACHE_LINE / 8;
    private static final int MAX_WAITERS = 8;
    private static final long WAITER_MASK = 7;
    private static final long WAIT_TIME = 50;
    private final LongAdder waitCount = new LongAdder();
    private final AtomicReferenceArray<Thread> waiter = new AtomicReferenceArray(8 + 2 * CACHE_LINE_REFS);
    long r1;
    long r2;
    long r3;
    long r4;
    long r5;
    long r6;
    long r7;
    private long waitCache = 0;
    long c1;
    long c2;
    long c3;
    long c4;
    long c5;
    long c6;
    long c7;
    long c8;

    @Override
    public abstract boolean test();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void awaitNanos(long timeout) throws InterruptedException {
        try {
            long waitCount;
            long waitSequence = waitCount = this.waitCount.sum();
            this.waitCount.increment();
            this.waitCache = waitCount + 1;
            long timeNow = System.nanoTime();
            long expires = timeNow + timeout;
            Thread t2 = Thread.currentThread();
            if (waitCount == 0) {
                int spin = 0;
                while (this.test() && expires > timeNow && !t2.isInterrupted()) {
                    spin = Condition.progressiveYield(spin);
                    timeNow = System.nanoTime();
                }
                if (t2.isInterrupted()) {
                    throw new InterruptedException();
                }
                return;
            }
            int spin = 0;
            while (this.test() && !this.waiter.compareAndSet((int)(waitSequence++ & 7) + CACHE_LINE_REFS, (Thread)null, t2) && expires > timeNow) {
                if (spin < 2000) {
                    spin = Condition.progressiveYield(spin);
                } else {
                    LockSupport.parkNanos(400);
                }
                timeNow = System.nanoTime();
            }
            while (this.test() && this.waiter.get((int)(waitSequence - 1 & 7) + CACHE_LINE_REFS) == t2 && expires > timeNow && !t2.isInterrupted()) {
                LockSupport.parkNanos(expires - timeNow >> 2);
                timeNow = System.nanoTime();
            }
            if (t2.isInterrupted()) {
                while (!this.waiter.compareAndSet((int)(waitSequence - 1 & 7) + CACHE_LINE_REFS, t2, null) && this.waiter.get(CACHE_LINE_REFS) == t2) {
                    LockSupport.parkNanos(50);
                }
                throw new InterruptedException();
            }
            return;
        }
        finally {
            this.waitCount.decrement();
            this.waitCache = this.waitCount.sum();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void await() throws InterruptedException {
        try {
            long waitCount;
            long waitSequence = waitCount = this.waitCount.sum();
            this.waitCount.increment();
            this.waitCache = waitCount + 1;
            Thread t2 = Thread.currentThread();
            if (waitCount == 0) {
                int spin = 0;
                while (this.test() && !t2.isInterrupted()) {
                    spin = Condition.progressiveYield(spin);
                }
                if (t2.isInterrupted()) {
                    throw new InterruptedException();
                }
                return;
            }
            int spin = 0;
            while (this.test() && !this.waiter.compareAndSet((int)(waitSequence++ & 7) + CACHE_LINE_REFS, (Thread)null, t2) && !t2.isInterrupted()) {
                if (spin < 2000) {
                    spin = Condition.progressiveYield(spin);
                    continue;
                }
                LockSupport.parkNanos(400);
            }
            while (this.test() && this.waiter.get((int)(waitSequence - 1 & 7) + CACHE_LINE_REFS) == t2 && !t2.isInterrupted()) {
                LockSupport.parkNanos(1000000);
            }
            if (t2.isInterrupted()) {
                while (!this.waiter.compareAndSet((int)(waitSequence - 1 & 7) + CACHE_LINE_REFS, t2, null) && this.waiter.get(CACHE_LINE_REFS) == t2) {
                    LockSupport.parkNanos(50);
                }
                throw new InterruptedException();
            }
            return;
        }
        finally {
            this.waitCount.decrement();
            this.waitCache = this.waitCount.sum();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void signal() {
        if (this.waitCache <= 0) {
            this.waitCache = this.waitCount.sum();
            if (this.waitCache <= 0) return;
        }
        waitSequence = 0;
        do lbl-1000: // 3 sources:
        {
            if ((t = this.waiter.get((int)(waitSequence++ & 7) + AbstractWaitingCondition.CACHE_LINE_REFS)) == null) ** GOTO lbl15
            if (this.waiter.compareAndSet((int)(waitSequence - 1 & 7) + AbstractWaitingCondition.CACHE_LINE_REFS, t, null)) {
                LockSupport.unpark(t);
            } else {
                LockSupport.parkNanos(50);
            }
            if ((waitSequence & 7) == 7) return;
            this.waitCache = this.waitCount.sum();
            if (this.waitCache != 0) ** GOTO lbl-1000
            return;
lbl15: // 1 sources:
            if ((waitSequence & 7) == 7) return;
        } while ((this.waitCache = this.waitCount.sum()) != 0);
    }
}

