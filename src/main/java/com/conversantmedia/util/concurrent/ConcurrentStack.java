/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.AbstractCondition;
import com.conversantmedia.util.concurrent.AbstractSpinningCondition;
import com.conversantmedia.util.concurrent.AbstractWaitingCondition;
import com.conversantmedia.util.concurrent.BlockingStack;
import com.conversantmedia.util.concurrent.Condition;
import com.conversantmedia.util.concurrent.ContendedAtomicInteger;
import com.conversantmedia.util.concurrent.SequenceLock;
import com.conversantmedia.util.concurrent.SpinPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class ConcurrentStack<N>
implements BlockingStack<N> {
    private final int size;
    private final AtomicReferenceArray<N> stack;
    private final ContendedAtomicInteger stackTop = new ContendedAtomicInteger(0);
    private final SequenceLock seqLock = new SequenceLock();
    private final Condition stackNotFullCondition;
    private final Condition stackNotEmptyCondition;

    public ConcurrentStack(int size) {
        this(size, SpinPolicy.WAITING);
    }

    public ConcurrentStack(int size, SpinPolicy spinPolicy) {
        int stackSize;
        for (stackSize = 1; stackSize < size; stackSize <<= 1) {
        }
        this.size = stackSize;
        this.stack = new AtomicReferenceArray(stackSize);
        switch (spinPolicy) {
            case BLOCKING: {
                this.stackNotFullCondition = new StackNotFull();
                this.stackNotEmptyCondition = new StackNotEmpty();
                break;
            }
            case SPINNING: {
                this.stackNotFullCondition = new SpinningStackNotFull();
                this.stackNotEmptyCondition = new SpinningStackNotEmpty();
                break;
            }
            default: {
                this.stackNotFullCondition = new WaitingStackNotFull();
                this.stackNotEmptyCondition = new WaitingStackNotEmpty();
            }
        }
    }

    @Override
    public final boolean push(N n2, long time, TimeUnit unit) throws InterruptedException {
        long endDate = System.nanoTime() + unit.toNanos(time);
        while (!this.push(n2)) {
            if (endDate - System.nanoTime() < 0) {
                return false;
            }
            Condition.waitStatus(time, unit, this.stackNotFullCondition);
        }
        this.stackNotEmptyCondition.signal();
        return true;
    }

    @Override
    public final void pushInterruptibly(N n2) throws InterruptedException {
        while (!this.push(n2)) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            this.stackNotFullCondition.await();
        }
        this.stackNotEmptyCondition.signal();
    }

    @Override
    public final boolean contains(N n2) {
        if (n2 != null) {
            for (int i2 = 0; i2 < this.stackTop.get(); ++i2) {
                if (!n2.equals(this.stack.get(i2))) continue;
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final boolean push(N n2) {
        int spin = 0;
        do {
            long writeLock;
            if ((writeLock = this.seqLock.tryWriteLock()) > 0) {
                try {
                    int stackTop = this.stackTop.get();
                    if (this.size > stackTop) {
                        try {
                            this.stack.set(stackTop, n2);
                            this.stackNotEmptyCondition.signal();
                            boolean bl2 = true;
                            return bl2;
                        }
                        finally {
                            this.stackTop.set(stackTop + 1);
                        }
                    }
                    boolean bl3 = false;
                    return bl3;
                }
                finally {
                    this.seqLock.unlock(writeLock);
                }
            }
            spin = Condition.progressiveYield(spin);
        } while (true);
    }

    @Override
    public final N peek() {
        int spin = 0;
        do {
            long readLock = this.seqLock.readLock();
            int stackTop = this.stackTop.get();
            if (stackTop > 0) {
                N n2 = this.stack.get(stackTop - 1);
                if (this.seqLock.readLockHeld(readLock)) {
                    return n2;
                }
            } else {
                return null;
            }
            spin = Condition.progressiveYield(spin);
        } while (true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final N pop() {
        int spin = 0;
        do {
            long writeLock;
            if ((writeLock = this.seqLock.tryWriteLock()) > 0) {
                int stackTop = this.stackTop.get();
                int lastRef = stackTop - 1;
                if (stackTop > 0) {
                    try {
                        N n2 = this.stack.get(lastRef);
                        this.stack.set(lastRef, null);
                        this.stackNotFullCondition.signal();
                        N n3 = n2;
                        return n3;
                    }
                    finally {
                        this.stackTop.set(lastRef);
                    }
                }
                N n2 = null;
                return n2;
                finally {
                    this.seqLock.unlock(writeLock);
                }
            }
            spin = Condition.progressiveYield(spin);
        } while (true);
    }

    @Override
    public final N pop(long time, TimeUnit unit) throws InterruptedException {
        long endTime = System.nanoTime() + unit.toNanos(time);
        do {
            N n2;
            if ((n2 = this.pop()) != null) {
                this.stackNotFullCondition.signal();
                return n2;
            }
            if (endTime - System.nanoTime() < 0) {
                return null;
            }
            Condition.waitStatus(time, unit, this.stackNotEmptyCondition);
        } while (true);
    }

    @Override
    public final N popInterruptibly() throws InterruptedException {
        do {
            N n2;
            if ((n2 = this.pop()) != null) {
                this.stackNotFullCondition.signal();
                return n2;
            }
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            this.stackNotEmptyCondition.await();
        } while (true);
    }

    @Override
    public final int size() {
        return this.stackTop.get();
    }

    @Override
    public final int remainingCapacity() {
        return this.size - this.stackTop.get();
    }

    @Override
    public final boolean isEmpty() {
        return this.stackTop.get() == 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void clear() {
        int spin = 0;
        do {
            long writeLock;
            if ((writeLock = this.seqLock.tryWriteLock()) > 0) {
                int stackTop = this.stackTop.get();
                if (stackTop > 0) {
                    try {
                        for (int i2 = 0; i2 < stackTop; ++i2) {
                            this.stack.set(i2, null);
                        }
                        this.stackNotFullCondition.signal();
                        return;
                    }
                    finally {
                        this.stackTop.set(0);
                    }
                }
                return;
            }
            spin = Condition.progressiveYield(spin);
        } while (true);
    }

    private boolean isFull() {
        return this.size == this.stackTop.get();
    }

    private final class StackNotEmpty
    extends AbstractCondition {
        private StackNotEmpty() {
        }

        @Override
        public final boolean test() {
            return ConcurrentStack.this.isEmpty();
        }
    }

    private final class StackNotFull
    extends AbstractCondition {
        private StackNotFull() {
        }

        @Override
        public final boolean test() {
            return ConcurrentStack.this.isFull();
        }
    }

    private final class SpinningStackNotEmpty
    extends AbstractSpinningCondition {
        private SpinningStackNotEmpty() {
        }

        @Override
        public final boolean test() {
            return ConcurrentStack.this.isEmpty();
        }
    }

    private final class SpinningStackNotFull
    extends AbstractSpinningCondition {
        private SpinningStackNotFull() {
        }

        @Override
        public final boolean test() {
            return ConcurrentStack.this.isFull();
        }
    }

    private final class WaitingStackNotEmpty
    extends AbstractWaitingCondition {
        private WaitingStackNotEmpty() {
        }

        @Override
        public final boolean test() {
            return ConcurrentStack.this.isEmpty();
        }
    }

    private final class WaitingStackNotFull
    extends AbstractWaitingCondition {
        private WaitingStackNotFull() {
        }

        @Override
        public final boolean test() {
            return ConcurrentStack.this.isFull();
        }
    }

}

