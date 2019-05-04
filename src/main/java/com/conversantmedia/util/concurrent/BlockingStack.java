/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.concurrent;

import com.conversantmedia.util.collection.Stack;
import java.util.concurrent.TimeUnit;

public interface BlockingStack<N>
extends Stack<N> {
    public boolean push(N var1, long var2, TimeUnit var4) throws InterruptedException;

    public void pushInterruptibly(N var1) throws InterruptedException;

    public N pop(long var1, TimeUnit var3) throws InterruptedException;

    public N popInterruptibly() throws InterruptedException;
}

