/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.concurrent.ExecutionException;

public abstract class Waitable<T>
implements Runnable {
    Throwable t = null;
    T value = null;
    Status status = Status.WAITING;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void run() {
        Waitable waitable = this;
        synchronized (waitable) {
            if (this.status != Status.WAITING) {
                throw new IllegalStateException("Invalid state " + (Object)((Object)this.status));
            }
            this.status = Status.RUNNING;
        }
        try {
            this.value = this.evaluate();
        }
        catch (Throwable t2) {
            this.t = t2;
        }
        finally {
            waitable = this;
            synchronized (waitable) {
                this.status = Status.FINISHED;
                this.notifyAll();
            }
        }
    }

    protected abstract T evaluate();

    public synchronized T get() throws InterruptedException, ExecutionException {
        while (this.status != Status.FINISHED) {
            this.wait();
        }
        if (this.t != null) {
            throw new ExecutionException(this.t);
        }
        return this.value;
    }

    private static enum Status {
        WAITING,
        RUNNING,
        FINISHED;
        

        private Status() {
        }
    }

}

