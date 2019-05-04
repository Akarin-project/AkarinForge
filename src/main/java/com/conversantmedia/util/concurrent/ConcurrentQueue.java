/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

public interface ConcurrentQueue<E> {
    public boolean offer(E var1);

    public E poll();

    public E peek();

    public int size();

    public int capacity();

    public boolean isEmpty();

    public boolean contains(Object var1);

    public int remove(E[] var1);

    public void clear();
}

