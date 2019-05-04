/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.ConcurrentQueue;
import com.conversantmedia.util.concurrent.ContendedAtomicLong;

class MPMCConcurrentQueue<E>
implements ConcurrentQueue<E> {
    protected final int size;
    final long mask;
    final Cell<E>[] buffer;
    final ContendedAtomicLong head = new ContendedAtomicLong(0);
    final ContendedAtomicLong tail = new ContendedAtomicLong(0);

    public MPMCConcurrentQueue(int capacity) {
        int c2;
        for (c2 = 2; c2 < capacity; c2 <<= 1) {
        }
        this.size = c2;
        this.mask = (long)this.size - 1;
        this.buffer = new Cell[this.size];
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.buffer[i2] = new Cell(i2);
        }
    }

    @Override
    public boolean offer(E e2) {
        long tail = this.tail.get();
        do {
            Cell<E> cell = this.buffer[(int)(tail & this.mask)];
            long seq = cell.seq.get();
            long dif = seq - tail;
            if (dif == 0) {
                if (!this.tail.compareAndSet(tail, tail + 1)) continue;
                break;
            }
            if (dif < 0) {
                return false;
            }
            tail = this.tail.get();
        } while (true);
        cell.entry = e2;
        cell.seq.set(tail + 1);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public E poll() {
        long head = this.head.get();
        do {
            Cell<E> cell = this.buffer[(int)(head & this.mask)];
            long seq = cell.seq.get();
            long dif = seq - (head + 1);
            if (dif == 0) {
                if (!this.head.compareAndSet(head, head + 1)) continue;
                break;
            }
            if (dif < 0) {
                return null;
            }
            head = this.head.get();
        } while (true);
        try {
            Object seq = cell.entry;
            return (E)seq;
        }
        finally {
            cell.entry = null;
            cell.seq.set(head + this.mask + 1);
        }
    }

    @Override
    public final E peek() {
        return (E)this.buffer[(int)(this.head.get() & this.mask)].entry;
    }

    @Override
    public int remove(E[] e2) {
        int nRead = 0;
        while (nRead < e2.length && !this.isEmpty()) {
            E entry = this.poll();
            if (entry == null) continue;
            e2[nRead++] = entry;
        }
        return nRead;
    }

    @Override
    public final int size() {
        return (int)Math.max(this.tail.get() - this.head.get(), 0);
    }

    @Override
    public int capacity() {
        return this.size;
    }

    @Override
    public final boolean isEmpty() {
        return this.head.get() == this.tail.get();
    }

    @Override
    public void clear() {
        while (!this.isEmpty()) {
            this.poll();
        }
    }

    @Override
    public final boolean contains(Object o2) {
        for (int i2 = 0; i2 < this.size(); ++i2) {
            int slot = (int)(this.head.get() + (long)i2 & this.mask);
            if (this.buffer[slot].entry == null || !this.buffer[slot].entry.equals(o2)) continue;
            return true;
        }
        return false;
    }

    protected static final class Cell<R> {
        final ContendedAtomicLong seq = new ContendedAtomicLong(0);
        public long p1;
        public long p2;
        public long p3;
        public long p4;
        public long p5;
        public long p6;
        public long p7;
        R entry;
        public long a1;
        public long a2;
        public long a3;
        public long a4;
        public long a5;
        public long a6;
        public long a7;
        public long a8;

        Cell(long s2) {
            this.seq.set(s2);
            this.entry = null;
        }

        public long sumToAvoidOptimization() {
            return this.p1 + this.p2 + this.p3 + this.p4 + this.p5 + this.p6 + this.p7 + this.a1 + this.a2 + this.a3 + this.a4 + this.a5 + this.a6 + this.a7 + this.a8;
        }
    }

}

