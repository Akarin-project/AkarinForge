/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.concurrent.Condition;

public abstract class AbstractSpinningCondition
implements Condition {
    @Override
    public void awaitNanos(long timeout) throws InterruptedException {
        long timeNow = System.nanoTime();
        long expires = timeNow + timeout;
        Thread t2 = Thread.currentThread();
        while (this.test() && expires > timeNow && !t2.isInterrupted()) {
            timeNow = System.nanoTime();
            Condition.onSpinWait();
        }
        if (t2.isInterrupted()) {
            throw new InterruptedException();
        }
    }

    @Override
    public void await() throws InterruptedException {
        Thread t2 = Thread.currentThread();
        while (this.test() && !t2.isInterrupted()) {
            Condition.onSpinWait();
        }
        if (t2.isInterrupted()) {
            throw new InterruptedException();
        }
    }

    @Override
    public void signal() {
    }
}

