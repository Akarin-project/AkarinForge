/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.AbstractCondition;
import com.conversantmedia.util.concurrent.AbstractSpinningCondition;
import com.conversantmedia.util.concurrent.AbstractWaitingCondition;
import com.conversantmedia.util.concurrent.ConcurrentQueue;
import com.conversantmedia.util.concurrent.Condition;
import com.conversantmedia.util.concurrent.ContendedAtomicLong;
import com.conversantmedia.util.concurrent.MPMCConcurrentQueue;
import com.conversantmedia.util.concurrent.SpinPolicy;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class MPMCBlockingQueue<E>
extends MPMCConcurrentQueue<E>
implements Serializable,
Iterable<E>,
Collection<E>,
BlockingQueue<E>,
Queue<E>,
ConcurrentQueue<E> {
    protected final Condition queueNotFullCondition;
    protected final Condition queueNotEmptyCondition;

    public MPMCBlockingQueue(int capacity) {
        this(capacity, SpinPolicy.WAITING);
    }

    public MPMCBlockingQueue(int capacity, SpinPolicy spinPolicy) {
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

    public MPMCBlockingQueue(int capacity, Collection<? extends E> c2) {
        this(capacity);
        for (E e2 : c2) {
            this.offer(e2);
        }
    }

    @Override
    public final boolean offer(E e2) {
        if (super.offer(e2)) {
            this.queueNotEmptyCondition.signal();
            return true;
        }
        this.queueNotEmptyCondition.signal();
        return false;
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
        int nRead = 0;
        while (!this.isEmpty() && maxElements > 0) {
            E e2 = this.poll();
            if (e2 == null) continue;
            c2.add(e2);
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
        throw new UnsupportedOperationException();
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
        for (E e2 : c2) {
            if (this.offer(e2)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new RingIter();
    }

    private final boolean isFull() {
        long queueStart = this.tail.get() - (long)this.size;
        return this.head.get() == queueStart;
    }

    private final class SpinningQueueNotEmpty
    extends AbstractSpinningCondition {
        private SpinningQueueNotEmpty() {
        }

        @Override
        public final boolean test() {
            return MPMCBlockingQueue.this.isEmpty();
        }
    }

    private final class SpinningQueueNotFull
    extends AbstractSpinningCondition {
        private SpinningQueueNotFull() {
        }

        @Override
        public final boolean test() {
            return MPMCBlockingQueue.this.isFull();
        }
    }

    private final class WaitingQueueNotEmpty
    extends AbstractWaitingCondition {
        private WaitingQueueNotEmpty() {
        }

        @Override
        public final boolean test() {
            return MPMCBlockingQueue.this.isEmpty();
        }
    }

    private final class WaitingQueueNotFull
    extends AbstractWaitingCondition {
        private WaitingQueueNotFull() {
        }

        @Override
        public final boolean test() {
            return MPMCBlockingQueue.this.isFull();
        }
    }

    private final class QueueNotEmpty
    extends AbstractCondition {
        private QueueNotEmpty() {
        }

        @Override
        public final boolean test() {
            return MPMCBlockingQueue.this.isEmpty();
        }
    }

    private final class QueueNotFull
    extends AbstractCondition {
        private QueueNotFull() {
        }

        @Override
        public final boolean test() {
            return MPMCBlockingQueue.this.isFull();
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
            return this.dx < MPMCBlockingQueue.this.size();
        }

        @Override
        public E next() {
            long pollPos = MPMCBlockingQueue.this.head.get();
            int slot = (int)(pollPos + (long)this.dx++ & MPMCBlockingQueue.this.mask);
            this.lastObj = MPMCBlockingQueue.this.buffer[slot].entry;
            return this.lastObj;
        }

        @Override
        public void remove() {
            MPMCBlockingQueue.this.remove(this.lastObj);
        }
    }

}

