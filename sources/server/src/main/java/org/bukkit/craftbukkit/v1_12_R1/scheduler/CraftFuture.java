/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.scheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftTask;
import org.bukkit.plugin.Plugin;

class CraftFuture<T>
extends CraftTask
implements Future<T> {
    private final Callable<T> callable;
    private T value;
    private Exception exception = null;

    CraftFuture(Callable<T> callable, Plugin plugin, int id2) {
        super(plugin, null, id2, -1);
        this.callable = callable;
    }

    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        if (this.getPeriod() != -1) {
            return false;
        }
        this.setPeriod(-2);
        return true;
    }

    @Override
    public boolean isDone() {
        long period = this.getPeriod();
        return period != -1 && period != -3;
    }

    @Override
    public T get() throws CancellationException, InterruptedException, ExecutionException {
        try {
            return this.get(0, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException e2) {
            throw new Error(e2);
        }
    }

    @Override
    public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long timestamp;
        timeout = unit.toMillis(timeout);
        long period = this.getPeriod();
        long l2 = timestamp = timeout > 0 ? System.currentTimeMillis() : 0;
        while (period == -1 || period == -3) {
            this.wait(timeout);
            period = this.getPeriod();
            if (period != -1 && period != -3) break;
            if (timeout == 0) continue;
            long l3 = timestamp;
            timestamp = System.currentTimeMillis();
            if ((timeout += l3 - timestamp) > 0) continue;
            throw new TimeoutException();
        }
        if (period == -2) {
            throw new CancellationException();
        }
        if (period == -4) {
            if (this.exception == null) {
                return this.value;
            }
            throw new ExecutionException(this.exception);
        }
        throw new IllegalStateException("Expected -1 to -4, got " + period);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        CraftFuture craftFuture = this;
        synchronized (craftFuture) {
            if (this.getPeriod() == -2) {
                return;
            }
            this.setPeriod(-3);
        }
        try {
            this.value = this.callable.call();
        }
        catch (Exception e2) {
            this.exception = e2;
        }
        finally {
            CraftFuture e2 = this;
            synchronized (e2) {
                this.setPeriod(-4);
                this.notifyAll();
            }
        }
    }

    @Override
    synchronized boolean cancel0() {
        if (this.getPeriod() != -1) {
            return false;
        }
        this.setPeriod(-2);
        this.notifyAll();
        return true;
    }
}

