package io.akarin.forge.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;

public class CachedSizeConcurrentLinkedQueue<E> extends ConcurrentLinkedQueue<E> {
    private static final long serialVersionUID = 1827077420088366210L;
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
        E result = super.poll();
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
