/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.Capacity;
import com.conversantmedia.util.concurrent.ConcurrentQueue;
import java.util.concurrent.atomic.LongAdder;

public class PushPullConcurrentQueue<E>
implements ConcurrentQueue<E> {
    final int size;
    final long mask;
    final LongAdder tail = new LongAdder();
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

    public PushPullConcurrentQueue(int capacity) {
        this.size = Capacity.getCapacity(capacity);
        this.mask = this.size - 1;
        this.buffer = new Object[this.size];
    }

    @Override
    public boolean offer(E e2) {
        if (e2 != null) {
            long tail = this.tail.sum();
            long queueStart = tail - (long)this.size;
            if (this.headCache > queueStart || (this.headCache = this.head.sum()) > queueStart) {
                int dx2 = (int)(tail & this.mask);
                this.buffer[dx2] = e2;
                this.tail.increment();
                return true;
            }
            return false;
        }
        throw new NullPointerException("Invalid element");
    }

    @Override
    public E poll() {
        long head = this.head.sum();
        if (head < this.tailCache || head < (this.tailCache = this.tail.sum())) {
            int dx2 = (int)(head & this.mask);
            E e2 = this.buffer[dx2];
            this.buffer[dx2] = null;
            this.head.increment();
            return e2;
        }
        return null;
    }

    @Override
    public int remove(E[] e2) {
        int n2 = 0;
        this.headCache = this.head.sum();
        int nMax = e2.length;
        long end = this.tail.sum();
        for (long i2 = this.headCache; n2 < nMax && i2 < end; ++i2) {
            int dx2 = (int)(i2 & this.mask);
            e2[n2++] = this.buffer[dx2];
            this.buffer[dx2] = null;
        }
        this.head.add(n2);
        return n2;
    }

    @Override
    public void clear() {
        for (int i2 = 0; i2 < this.buffer.length; ++i2) {
            this.buffer[i2] = null;
        }
        this.head.add(this.tail.sum() - this.head.sum());
    }

    @Override
    public final E peek() {
        return this.buffer[(int)(this.head.sum() & this.mask)];
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
    public final boolean contains(Object o2) {
        if (o2 != null) {
            long end = this.tail.sum();
            for (long i2 = this.head.sum(); i2 < end; ++i2) {
                E e2 = this.buffer[(int)(i2 & this.mask)];
                if (!o2.equals(e2)) continue;
                return true;
            }
        }
        return false;
    }

    long sumToAvoidOptimization() {
        return this.p1 + this.p2 + this.p3 + this.p4 + this.p5 + this.p6 + this.p7 + this.a1 + this.a2 + this.a3 + this.a4 + this.a5 + this.a6 + this.a7 + this.a8 + this.r1 + this.r2 + this.r3 + this.r4 + this.r5 + this.r6 + this.r7 + this.c1 + this.c2 + this.c3 + this.c4 + this.c5 + this.c6 + this.c7 + this.c8 + this.headCache + this.tailCache;
    }
}

