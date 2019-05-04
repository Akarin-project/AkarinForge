/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import org.apache.commons.lang3.Validate;

public final class AsynchronousExecutor<P, T, C, E extends Throwable> {
    static final AtomicIntegerFieldUpdater STATE_FIELD = AtomicIntegerFieldUpdater.newUpdater(Task.class, "state");
    final CallBackProvider<P, T, C, E> provider;
    final Queue<AsynchronousExecutor<P, T, C, E>> finished = new ConcurrentLinkedQueue<AsynchronousExecutor<P, T, C, E>>();
    final Map<P, AsynchronousExecutor<P, T, C, E>> tasks = new HashMap<P, AsynchronousExecutor<P, T, C, E>>();
    final ThreadPoolExecutor pool;

    private static boolean set(Task $this, int expected, int value) {
        return STATE_FIELD.compareAndSet($this, expected, value);
    }

    public AsynchronousExecutor(CallBackProvider<P, T, C, E> provider, int coreSize) {
        Validate.notNull(provider, (String)"Provider cannot be null", (Object[])new Object[0]);
        this.provider = provider;
        this.pool = new ThreadPoolExecutor(coreSize, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), provider);
    }

    public void add(P parameter, C callback) {
        Task task = (Task)((Object)this.tasks.get(parameter));
        if (task == null) {
            task = new Task(parameter);
            this.tasks.put(parameter, (AsynchronousExecutor<P, T, C, E>)((Object)task));
            this.pool.execute(task);
        }
        task.callbacks.add(callback);
    }

    public boolean drop(P parameter, C callback) throws IllegalStateException {
        Task task = (Task)((Object)this.tasks.get(parameter));
        if (task == null) {
            return true;
        }
        if (!task.callbacks.remove(callback)) {
            throw new IllegalStateException("Unknown " + callback + " for " + parameter);
        }
        if (task.callbacks.isEmpty()) {
            return task.drop();
        }
        return false;
    }

    public T get(P parameter) throws Throwable, IllegalStateException {
        Task task = (Task)((Object)this.tasks.get(parameter));
        if (task == null) {
            throw new IllegalStateException("Unknown " + parameter);
        }
        return task.get();
    }

    public T getSkipQueue(P parameter) throws Throwable {
        return this.skipQueue(parameter);
    }

    public T getSkipQueue(P parameter, C callback) throws Throwable {
        T object = this.skipQueue(parameter);
        this.provider.callStage3(parameter, object, callback);
        return object;
    }

    public /* varargs */ T getSkipQueue(P parameter, C ... callbacks) throws Throwable {
        CallBackProvider<P, T, C, E> provider = this.provider;
        T object = this.skipQueue(parameter);
        for (C callback : callbacks) {
            provider.callStage3(parameter, object, callback);
        }
        return object;
    }

    public T getSkipQueue(P parameter, Iterable<C> callbacks) throws Throwable {
        CallBackProvider<P, T, C, E> provider = this.provider;
        T object = this.skipQueue(parameter);
        for (C callback : callbacks) {
            provider.callStage3(parameter, object, callback);
        }
        return object;
    }

    private T skipQueue(P parameter) throws Throwable {
        Task task = (Task)((Object)this.tasks.get(parameter));
        if (task != null) {
            return task.get();
        }
        T object = this.provider.callStage1(parameter);
        this.provider.callStage2(parameter, object);
        return object;
    }

    public void finishActive() throws Throwable {
        Queue<AsynchronousExecutor<P, T, C, E>> finished = this.finished;
        while (!finished.isEmpty()) {
            ((Task)((Object)finished.poll())).finish();
        }
    }

    public void setActiveThreads(int coreSize) {
        this.pool.setCorePoolSize(coreSize);
    }

    class Task
    implements Runnable {
        static final int PENDING = 0;
        static final int STAGE_1_ASYNC = 1;
        static final int STAGE_1_SYNC = 2;
        static final int STAGE_1_COMPLETE = 3;
        static final int FINISHED = 4;
        volatile int state;
        final P parameter;
        T object;
        final List<C> callbacks;
        E t;

        Task(P parameter) {
            this.state = 0;
            this.callbacks = new LinkedList<C>();
            this.t = null;
            this.parameter = parameter;
        }

        @Override
        public void run() {
            if (this.initAsync()) {
                AsynchronousExecutor.this.finished.add((Task)this);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean initAsync() {
            if (AsynchronousExecutor.set(this, 0, 1)) {
                boolean ret = true;
                try {
                    this.init();
                }
                finally {
                    if (!AsynchronousExecutor.set(this, 1, 3)) {
                        Task task = this;
                        synchronized (task) {
                            if (this.state != 2) {
                                this.notifyAll();
                            }
                            this.state = 3;
                        }
                        ret = false;
                    }
                }
                return ret;
            }
            return false;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void initSync() {
            if (AsynchronousExecutor.set(this, 0, 3)) {
                this.init();
            } else if (AsynchronousExecutor.set(this, 1, 2)) {
                Task task = this;
                synchronized (task) {
                    if (AsynchronousExecutor.set(this, 2, 0)) {
                        while (this.state != 3) {
                            try {
                                this.wait();
                                continue;
                            }
                            catch (InterruptedException e2) {
                                Thread.currentThread().interrupt();
                                throw new RuntimeException("Unable to handle interruption on " + this.parameter, e2);
                            }
                        }
                    }
                }
            }
        }

        void init() {
            try {
                this.object = AsynchronousExecutor.this.provider.callStage1(this.parameter);
            }
            catch (Throwable t2) {
                this.t = t2;
            }
        }

        T get() throws Throwable {
            this.initSync();
            if (this.callbacks.isEmpty()) {
                this.callbacks.add((Task)this);
            }
            this.finish();
            return this.object;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void finish() throws Throwable {
            switch (this.state) {
                default: {
                    throw new IllegalStateException("Attempting to finish unprepared(" + this.state + ") task(" + this.parameter + ")");
                }
                case 3: {
                    try {
                        if (this.t != null) {
                            throw this.t;
                        }
                        if (this.callbacks.isEmpty()) {
                            return;
                        }
                        CallBackProvider provider = AsynchronousExecutor.this.provider;
                        P parameter = this.parameter;
                        T object = this.object;
                        provider.callStage2(parameter, object);
                        for (C callback : this.callbacks) {
                            if (callback == this) continue;
                            provider.callStage3(parameter, object, callback);
                        }
                        break;
                    }
                    finally {
                        AsynchronousExecutor.this.tasks.remove(this.parameter);
                        this.state = 4;
                    }
                }
                case 4: 
            }
        }

        boolean drop() {
            if (AsynchronousExecutor.set(this, 0, 4)) {
                AsynchronousExecutor.this.tasks.remove(this.parameter);
                return true;
            }
            return false;
        }
    }

    public static interface CallBackProvider<P, T, C, E extends Throwable>
    extends ThreadFactory {
        public T callStage1(P var1) throws Throwable;

        public void callStage2(P var1, T var2) throws Throwable;

        public void callStage3(P var1, T var2, C var3) throws Throwable;
    }

}

