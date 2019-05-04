/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.Condition;
import java.util.concurrent.locks.ReentrantLock;

abstract class AbstractCondition
implements Condition {
    private final ReentrantLock queueLock = new ReentrantLock();
    private final java.util.concurrent.locks.Condition condition = this.queueLock.newCondition();

    AbstractCondition() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void awaitNanos(long timeout) throws InterruptedException {
        long remaining = timeout;
        this.queueLock.lock();
        try {
            while (this.test() && remaining > 0) {
                remaining = this.condition.awaitNanos(remaining);
            }
        }
        finally {
            this.queueLock.unlock();
        }
    }

    @Override
    public void await() throws InterruptedException {
        this.queueLock.lock();
        try {
            while (this.test()) {
                this.condition.await();
            }
        }
        finally {
            this.queueLock.unlock();
        }
    }

    @Override
    public void signal() {
        this.queueLock.lock();
        try {
            this.condition.signalAll();
        }
        finally {
            this.queueLock.unlock();
        }
    }
}

