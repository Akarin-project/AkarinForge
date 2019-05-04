/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.Capacity;
import com.conversantmedia.util.concurrent.ConcurrentQueue;
import com.conversantmedia.util.concurrent.Condition;
import com.conversantmedia.util.concurrent.ContendedAtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class MultithreadConcurrentQueue<E>
implements ConcurrentQueue<E> {
    protected final int size;
    final long mask;
    final LongAdder tail = new LongAdder();
    final ContendedAtomicLong tailCursor = new ContendedAtomicLong(0);
    long p1;
    long p2;
    long p3;
    long p4;
    long p5;
    long p6;
    long p7;
    long tailCache = 0;
    long a1;
    long a2;
    long a3;
    long a4;
    long a5;
    long a6;
    long a7;
    long a8;
    final E[] buffer;
    long r1;
    long r2;
    long r3;
    long r4;
    long r5;
    long r6;
    long r7;
    long headCache = 0;
    long c1;
    long c2;
    long c3;
    long c4;
    long c5;
    long c6;
    long c7;
    long c8;
    final LongAdder head = new LongAdder();
    final ContendedAtomicLong headCursor = new ContendedAtomicLong(0);

    public MultithreadConcurrentQueue(int capacity) {
        this.size = Capacity.getCapacity(capacity);
        this.mask = (long)this.size - 1;
        this.buffer = new Object[this.size];
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean offer(E e2) {
        int spin = 0;
        do {
            long tailSeq;
            long queueStart;
            if (this.headCache > (queueStart = (tailSeq = this.tail.sum()) - (long)this.size) || (this.headCache = this.head.sum()) > queueStart) {
                if (this.tailCursor.compareAndSet(tailSeq, tailSeq + 1)) {
                    try {
                        int tailSlot = (int)(tailSeq & this.mask);
                        this.buffer[tailSlot] = e2;
                        boolean bl2 = true;
                        return bl2;
                    }
                    finally {
                        this.tail.increment();
                    }
                }
            } else {
                return false;
            }
            spin = Condition.progressiveYield(spin);
        } while (true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public E poll() {
        int spin = 0;
        do {
            long head;
            if (this.tailCache > (head = this.head.sum()) || (this.tailCache = this.tail.sum()) > head) {
                if (this.headCursor.compareAndSet(head, head + 1)) {
                    try {
                        int pollSlot = (int)(head & this.mask);
                        E pollObj = this.buffer[pollSlot];
                        this.buffer[pollSlot] = null;
                        E e2 = pollObj;
                        return e2;
                    }
                    finally {
                        this.head.increment();
                    }
                }
            } else {
                return null;
            }
            spin = Condition.progressiveYield(spin);
        } while (true);
    }

    @Override
    public final E peek() {
        return this.buffer[(int)(this.head.sum() & this.mask)];
    }

    @Override
    public int remove(E[] e2) {
        int maxElements = e2.length;
        int spin = 0;
        do {
            long pollPos = this.head.sum();
            int nToRead = Math.min((int)(this.tail.sum() - pollPos), maxElements);
            if (nToRead > 0) {
                for (int i2 = 0; i2 < nToRead; ++i2) {
                    int pollSlot = (int)(pollPos + (long)i2 & this.mask);
                    e2[i2] = this.buffer[pollSlot];
                }
                if (this.headCursor.compareAndSet(pollPos, pollPos + (long)nToRead)) {
                    this.head.add(nToRead);
                    return nToRead;
                }
            } else {
                return 0;
            }
            spin = Condition.progressiveYield(spin);
        } while (true);
    }

    @Override
    public final int size() {
        return (int)Math.max(this.tail.sum() - this.head.sum(), 0);
    }

    @Override
    public int capacity() {
        return this.size;
    }

    @Override
    public final boolean isEmpty() {
        return this.tail.sum() == this.head.sum();
    }

    @Override
    public void clear() {
        int spin = 0;
        do {
            long head;
            if (this.headCursor.compareAndSet(head = this.head.sum(), head + 1)) {
                do {
                    long tail;
                    if (this.tailCursor.compareAndSet(tail = this.tail.sum(), tail + 1)) {
                        for (int i2 = 0; i2 < this.buffer.length; ++i2) {
                            this.buffer[i2] = null;
                        }
                        this.tail.increment();
                        this.head.add(tail - head + 1);
                        this.headCursor.set(tail + 1);
                        return;
                    }
                    spin = Condition.progressiveYield(spin);
                } while (true);
            }
            spin = Condition.progressiveYield(spin);
        } while (true);
    }

    @Override
    public final boolean contains(Object o2) {
        for (int i2 = 0; i2 < this.size(); ++i2) {
            int slot = (int)(this.head.sum() + (long)i2 & this.mask);
            if (this.buffer[slot] == null || !this.buffer[slot].equals(o2)) continue;
            return true;
        }
        return false;
    }

    long sumToAvoidOptimization() {
        return this.p1 + this.p2 + this.p3 + this.p4 + this.p5 + this.p6 + this.p7 + this.a1 + this.a2 + this.a3 + this.a4 + this.a5 + this.a6 + this.a7 + this.a8 + this.r1 + this.r2 + this.r3 + this.r4 + this.r5 + this.r6 + this.r7 + this.c1 + this.c2 + this.c3 + this.c4 + this.c5 + this.c6 + this.c7 + this.c8 + this.headCache + this.tailCache;
    }
}

