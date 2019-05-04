/*
 * Decompiled with CFR 0_119.
 */
package catserver.server.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;

public class CachedSizeConcurrentLinkedQueue<E>
extends ConcurrentLinkedQueue<E> {
    private final LongAdder cachedSize = new LongAdder();

    @Override
    public boolean add(E e2) {
        boolean result = super.add(e2);
        if (result) {
            this.cachedSize.increment();
        }
        return result;
    }

    @Override
    public E poll() {
        Object result = super.poll();
        if (result != null) {
            this.cachedSize.decrement();
        }
        return result;
    }

    @Override
    public int size() {
        return this.cachedSize.intValue();
    }
}

