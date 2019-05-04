/*
 * Decompiled with CFR 0_119.
 */
package com.conversantmedia.util.collection;

import com.conversantmedia.util.collection.Stack;

public class FixedStack<N>
implements Stack<N> {
    private final int size;
    private final int mask;
    private final N[] stack;
    private int stackTop;

    public FixedStack(int size) {
        int stackSize;
        for (stackSize = 1; stackSize < size; stackSize <<= 1) {
        }
        this.size = stackSize;
        this.mask = this.size - 1;
        this.stack = new Object[stackSize];
        this.stackTop = 0;
    }

    @Override
    public boolean push(N n2) {
        if (this.stackTop < this.size) {
            this.stack[this.stackTop++ & this.mask] = n2;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(N n2) {
        for (int i2 = 0; i2 < this.stackTop; ++i2) {
            if (!this.stack[i2].equals(n2)) continue;
            return true;
        }
        return false;
    }

    @Override
    public N peek() {
        return this.stack[this.stackTop - 1 & this.mask];
    }

    @Override
    public N pop() {
        try {
            N n2 = this.stack[--this.stackTop & this.mask];
            return n2;
        }
        finally {
            this.stack[this.stackTop & this.mask] = null;
        }
    }

    @Override
    public int size() {
        return this.stackTop;
    }

    @Override
    public int remainingCapacity() {
        return this.size - this.stackTop;
    }

    @Override
    public boolean isEmpty() {
        return this.stackTop == 0;
    }

    @Override
    public void clear() {
        this.stackTop = 0;
    }
}

