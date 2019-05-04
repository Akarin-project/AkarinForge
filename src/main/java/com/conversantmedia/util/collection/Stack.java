/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.collection;

public interface Stack<N> {
    public boolean contains(N var1);

    public boolean push(N var1);

    public N peek();

    public N pop();

    public int size();

    public int remainingCapacity();

    public boolean isEmpty();

    public void clear();
}

