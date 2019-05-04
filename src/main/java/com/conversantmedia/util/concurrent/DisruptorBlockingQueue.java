/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.AbstractCondition;
import com.conversantmedia.util.concurrent.AbstractSpinningCondition;
import com.conversantmedia.util.concurrent.AbstractWaitingCondition;
import com.conversantmedia.util.concurrent.Condition;
import com.conversantmedia.util.concurrent.ContendedAtomicLong;
import com.conversantmedia.util.concurrent.MultithreadConcurrentQueue;
import com.conversantmedia.util.concurrent.SpinPolicy;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public final class DisruptorBlockingQueue<E>
extends MultithreadConcurrentQueue<E>
implements Serializable,
Iterable<E>,
Collection<E>,
BlockingQueue<E>,
Queue<E> {
    protected final Condition queueNotFullCondition;
    protected final Condition queueNotEmptyCondition;

    public DisruptorBlockingQueue(int capacity) {
        this(capacity, SpinPolicy.WAITING);
    }

    public DisruptorBlockingQueue(int capacity, SpinPolicy spinPolicy) {
        super(capacity);
        switch (spinPolicy) {
            case BLOCKING: {
                this.queueNotFullCondition = new QueueNotFull();
                this.queueNotEmptyCondition = new QueueNotEmpty();
                break;
            }
            case SPINNING: {
                this.queueNotFullCondition = new SpinningQueueNotFull();
                this.queueNotEmptyCondition = new SpinningQueueNotEmpty();
                break;
            }
            default: {
                this.queueNotFullCondition = new WaitingQueueNotFull();
                this.queueNotEmptyCondition = new WaitingQueueNotEmpty();
            }
        }
    }

    public DisruptorBlockingQueue(int capacity, Collection<? extends E> c2) {
        this(capacity);
        for (E e2 : c2) {
            this.offer(e2);
        }
    }

    @Override
    public final boolean offer(E e2) {
        try {
            boolean bl2 = super.offer(e2);
            return bl2;
        }
        finally {
            this.queueNotEmptyCondition.signal();
        }
    }

    @Override
    public final E poll() {
        Object e2 = super.poll();
        this.queueNotFullCondition.signal();
        return e2;
    }

    @Override
    public int remove(E[] e2) {
        int n2 = super.remove(e2);
        this.queueNotFullCondition.signal();
        return n2;
    }

    @Override
    public E remove() {
        return this.poll();
    }

    @Override
    public E element() {
        Object val = this.peek();
        if (val != null) {
            return val;
        }
        throw new NoSuchElementException("No element found.");
    }

    @Override
    public void put(E e2) throws InterruptedException {
        while (!this.offer(e2)) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            this.queueNotFullCondition.await();
        }
    }

    @Override
    public boolean offer(E e2, long timeout, TimeUnit unit) throws InterruptedException {
        do {
            if (!this.offer(e2)) continue;
            return true;
        } while (Condition.waitStatus(timeout, unit, this.queueNotFullCondition));
        return false;
    }

    @Override
    public E take() throws InterruptedException {
        E pollObj;
        while ((pollObj = this.poll()) == null) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            this.queueNotEmptyCondition.await();
        }
        return pollObj;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        do {
            E pollObj;
            if ((pollObj = this.poll()) == null) continue;
            return pollObj;
        } while (Condition.waitStatus(timeout, unit, this.queueNotEmptyCondition));
        return null;
    }

    @Override
    public void clear() {
        super.clear();
        this.queueNotFullCondition.signal();
    }

    @Override
    public int remainingCapacity() {
        return this.size - this.size();
    }

    @Override
    public int drainTo(Collection<? super E> c2) {
        return this.drainTo(c2, this.size());
    }

    @Override
    public int drainTo(Collection<? super E> c2, int maxElements) {
        if (this == c2) {
            throw new IllegalArgumentException("Can not drain to self.");
        }
        Object[] pollObj = new Object[java.lang.Math.min(this.size(), maxElements)];
        int nEle = this.remove((E[])pollObj);
        int nRead = 0;
        for (int i2 = 0; i2 < nEle; ++i2) {
            if (!c2.add(pollObj[i2])) continue;
            ++nRead;
        }
        return nRead;
    }

    @Override
    public Object[] toArray() {
        Object[] e2 = new Object[this.size()];
        this.toArray(e2);
        return e2;
    }

    @Override
    public <T> T[] toArray(T[] a2) {
        this.remove((E[])a2);
        return a2;
    }

    @Override
    public boolean add(E e2) {
        if (this.offer(e2)) {
            return true;
        }
        throw new IllegalStateException("queue is full");
    }

    @Override
    public boolean remove(Object o2) {
        long head;
        long tail;
        while (!this.headCursor.compareAndSet(head = this.head.sum(), head + 1)) {
        }
        while (!this.tailCursor.compareAndSet(tail = this.tail.sum(), tail + 1)) {
        }
        int n2 = 0;
        for (int i2 = 0; i2 < this.size(); ++i2) {
            int slot = (int)(this.head.sum() + (long)i2 & this.mask);
            if (this.buffer[slot] == null || !this.buffer[slot].equals(o2)) continue;
            ++n2;
            for (int j2 = i2; j2 > 0; --j2) {
                int cSlot = (int)(this.head.sum() + (long)j2 - 1 & this.mask);
                int nextSlot = (int)(this.head.sum() + (long)j2 & this.mask);
                this.buffer[nextSlot] = this.buffer[cSlot];
            }
        }
        if (n2 > 0) {
            this.headCursor.set(head + (long)n2);
            this.tailCursor.set(tail);
            this.head.add(n2);
            this.queueNotFullCondition.signal();
            return true;
        }
        this.tailCursor.set(tail);
        this.headCursor.set(head);
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c2) {
        for (Object o2 : c2) {
            if (this.contains(o2)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c2) {
        boolean rc2 = false;
        for (E e2 : c2) {
            if (!this.offer(e2)) continue;
            rc2 = true;
        }
        return rc2;
    }

    @Override
    public boolean removeAll(Collection<?> c2) {
        boolean isChanged = false;
        for (Object o2 : c2) {
            if (!this.remove(o2)) continue;
            isChanged = true;
        }
        return isChanged;
    }

    @Override
    public boolean retainAll(Collection<?> c2) {
        boolean isChanged = false;
        for (int i2 = 0; i2 < this.size(); ++i2) {
            int headSlot = (int)(this.head.sum() + (long)i2 & this.mask);
            if (this.buffer[headSlot] == null || c2.contains(this.buffer[headSlot]) || !this.remove(this.buffer[headSlot])) continue;
            --i2;
            isChanged = true;
        }
        return isChanged;
    }

    @Override
    public Iterator<E> iterator() {
        return new RingIter();
    }

    private boolean isFull() {
        long queueStart = this.tail.sum() - (long)this.size;
        return this.head.sum() == queueStart;
    }

    private final class SpinningQueueNotEmpty
    extends AbstractSpinningCondition {
        private SpinningQueueNotEmpty() {
        }

        @Override
        public final boolean test() {
            return DisruptorBlockingQueue.this.isEmpty();
        }
    }

    private final class SpinningQueueNotFull
    extends AbstractSpinningCondition {
        private SpinningQueueNotFull() {
        }

        @Override
        public final boolean test() {
            return DisruptorBlockingQueue.this.isFull();
        }
    }

    private final class WaitingQueueNotEmpty
    extends AbstractWaitingCondition {
        private WaitingQueueNotEmpty() {
        }

        @Override
        public final boolean test() {
            return DisruptorBlockingQueue.this.isEmpty();
        }
    }

    private final class WaitingQueueNotFull
    extends AbstractWaitingCondition {
        private WaitingQueueNotFull() {
        }

        @Override
        public final boolean test() {
            return DisruptorBlockingQueue.this.isFull();
        }
    }

    private final class QueueNotEmpty
    extends AbstractCondition {
        private QueueNotEmpty() {
        }

        @Override
        public final boolean test() {
            return DisruptorBlockingQueue.this.isEmpty();
        }
    }

    private final class QueueNotFull
    extends AbstractCondition {
        private QueueNotFull() {
        }

        @Override
        public final boolean test() {
            return DisruptorBlockingQueue.this.isFull();
        }
    }

    private final class RingIter
    implements Iterator<E> {
        int dx;
        E lastObj;

        private RingIter() {
            this.dx = 0;
            this.lastObj = null;
        }

        @Override
        public boolean hasNext() {
            return this.dx < DisruptorBlockingQueue.this.size();
        }

        @Override
        public E next() {
            long pollPos = DisruptorBlockingQueue.this.head.sum();
            int slot = (int)(pollPos + (long)this.dx++ & DisruptorBlockingQueue.this.mask);
            this.lastObj = DisruptorBlockingQueue.this.buffer[slot];
            return this.lastObj;
        }

        @Override
        public void remove() {
            DisruptorBlockingQueue.this.remove(this.lastObj);
        }
    }

}

