/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftAsyncDebugger;
import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftAsyncTask;
import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftFuture;
import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftTask;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.spigotmc.CustomTimingsHandler;

public class CraftScheduler
implements BukkitScheduler {
    private final AtomicInteger ids = new AtomicInteger(1);
    private volatile CraftTask head = new CraftTask();
    private final AtomicReference<CraftTask> tail = new AtomicReference<CraftTask>(this.head);
    private final PriorityQueue<CraftTask> pending;
    private final List<CraftTask> temp;
    private final ConcurrentHashMap<Integer, CraftTask> runners;
    private volatile CraftTask currentTask;
    private volatile int currentTick;
    private final Executor executor;
    private CraftAsyncDebugger debugHead;
    private CraftAsyncDebugger debugTail;
    private static final int RECENT_TICKS = 30;

    public CraftScheduler() {
        this.pending = new PriorityQueue(10, new Comparator<CraftTask>(){

            @Override
            public int compare(CraftTask o1, CraftTask o2) {
                int value = Long.compare(o1.getNextRun(), o2.getNextRun());
                return value != 0 ? value : Integer.compare(o1.getTaskId(), o2.getTaskId());
            }
        });
        this.temp = new ArrayList<CraftTask>();
        this.runners = new ConcurrentHashMap();
        this.currentTask = null;
        this.currentTick = -1;
        this.executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Craft Scheduler Thread - %1$d").build());
        this.debugTail = this.debugHead = new CraftAsyncDebugger(-1, null, null){

            @Override
            StringBuilder debugTo(StringBuilder string) {
                return string;
            }
        };
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task) {
        return this.scheduleSyncDelayedTask(plugin, task, 0);
    }

    @Override
    public BukkitTask runTask(Plugin plugin, Runnable runnable) {
        return this.runTaskLater(plugin, runnable, 0);
    }

    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task) {
        return this.scheduleAsyncDelayedTask(plugin, task, 0);
    }

    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        return this.runTaskLaterAsynchronously(plugin, runnable, 0);
    }

    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return this.scheduleSyncRepeatingTask(plugin, task, delay, -1);
    }

    @Override
    public BukkitTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
        return this.runTaskTimer(plugin, runnable, delay, -1);
    }

    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay) {
        return this.scheduleAsyncRepeatingTask(plugin, task, delay, -1);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, -1);
    }

    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period) {
        return this.runTaskTimer(plugin, runnable, delay, period).getTaskId();
    }

    @Override
    public BukkitTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
        CraftScheduler.validate(plugin, runnable);
        if (delay < 0) {
            delay = 0;
        }
        if (period == 0) {
            period = 1;
        } else if (period < -1) {
            period = -1;
        }
        return this.handle(new CraftTask(plugin, runnable, this.nextId(), period), delay);
    }

    @Deprecated
    @Override
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period) {
        return this.runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
        CraftScheduler.validate(plugin, runnable);
        if (delay < 0) {
            delay = 0;
        }
        if (period == 0) {
            period = 1;
        } else if (period < -1) {
            period = -1;
        }
        return this.handle(new CraftAsyncTask(this.runners, plugin, runnable, this.nextId(), period), delay);
    }

    @Override
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task) {
        CraftScheduler.validate(plugin, task);
        CraftFuture<T> future = new CraftFuture<T>(task, plugin, this.nextId());
        this.handle(future, 0);
        return future;
    }

    @Override
    public void cancelTask(final int taskId) {
        if (taskId <= 0) {
            return;
        }
        CraftTask task = this.runners.get(taskId);
        if (task != null) {
            task.cancel0();
        }
        task = new CraftTask(new Runnable(){

            @Override
            public void run() {
                if (!this.check(CraftScheduler.this.temp)) {
                    this.check(CraftScheduler.this.pending);
                }
            }

            private boolean check(Iterable<CraftTask> collection) {
                Iterator<CraftTask> tasks = collection.iterator();
                while (tasks.hasNext()) {
                    CraftTask task = tasks.next();
                    if (task.getTaskId() != taskId) continue;
                    task.cancel0();
                    tasks.remove();
                    if (task.isSync()) {
                        CraftScheduler.this.runners.remove(taskId);
                    }
                    return true;
                }
                return false;
            }
        });
        this.handle(task, 0);
        for (CraftTask taskPending = this.head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                return;
            }
            if (taskPending.getTaskId() != taskId) continue;
            taskPending.cancel0();
        }
    }

    @Override
    public void cancelTasks(final Plugin plugin) {
        Validate.notNull((Object)plugin, (String)"Cannot cancel tasks of null plugin");
        CraftTask task = new CraftTask(new Runnable(){

            @Override
            public void run() {
                this.check(CraftScheduler.this.pending);
                this.check(CraftScheduler.this.temp);
            }

            void check(Iterable<CraftTask> collection) {
                Iterator<CraftTask> tasks = collection.iterator();
                while (tasks.hasNext()) {
                    CraftTask task = tasks.next();
                    if (!task.getOwner().equals(plugin)) continue;
                    task.cancel0();
                    tasks.remove();
                    if (!task.isSync()) continue;
                    CraftScheduler.this.runners.remove(task.getTaskId());
                }
            }
        });
        this.handle(task, 0);
        for (Object taskPending = this.head.getNext(); taskPending != null && taskPending != task; taskPending = ((CraftTask) taskPending).getNext()) {
            if (((CraftTask) taskPending).getTaskId() == -1 || !((CraftTask) taskPending).getOwner().equals(plugin)) continue;
            ((CraftAsyncTask) taskPending).cancel0();
        }
        for (CraftTask runner : this.runners.values()) {
            if (!runner.getOwner().equals(plugin)) continue;
            runner.cancel0();
        }
    }

    @Override
    public void cancelAllTasks() {
        CraftTask task = new CraftTask(new Runnable(){

            @Override
            public void run() {
                Iterator it2 = CraftScheduler.this.runners.values().iterator();
                while (it2.hasNext()) {
                    CraftTask task = (CraftTask)it2.next();
                    task.cancel0();
                    if (!task.isSync()) continue;
                    it2.remove();
                }
                CraftScheduler.this.pending.clear();
                CraftScheduler.this.temp.clear();
            }
        });
        this.handle(task, 0);
        for (Object taskPending = this.head.getNext(); taskPending != null && taskPending != task; taskPending = ((CraftTask) taskPending).getNext()) {
            ((CraftAsyncTask) taskPending).cancel0();
        }
        for (CraftTask runner : this.runners.values()) {
            runner.cancel0();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isCurrentlyRunning(int taskId) {
        CraftTask task = this.runners.get(taskId);
        if (task == null) {
            return false;
        }
        if (task.isSync()) {
            return task == this.currentTask;
        }
        CraftAsyncTask asyncTask = (CraftAsyncTask)task;
        LinkedList<BukkitWorker> linkedList = asyncTask.getWorkers();
        synchronized (linkedList) {
            return !asyncTask.getWorkers().isEmpty();
        }
    }

    @Override
    public boolean isQueued(int taskId) {
        CraftTask task;
        if (taskId <= 0) {
            return false;
        }
        for (task = this.head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() != taskId) continue;
            return task.getPeriod() >= -1;
        }
        task = this.runners.get(taskId);
        return task != null && task.getPeriod() >= -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<BukkitWorker> getActiveWorkers() {
        ArrayList<BukkitWorker> workers = new ArrayList<BukkitWorker>();
        for (CraftTask taskObj : this.runners.values()) {
            if (taskObj.isSync()) continue;
            CraftAsyncTask task = (CraftAsyncTask)taskObj;
            LinkedList<BukkitWorker> linkedList = task.getWorkers();
            synchronized (linkedList) {
                workers.addAll(task.getWorkers());
                continue;
            }
        }
        return workers;
    }

    @Override
    public List<BukkitTask> getPendingTasks() {
        ArrayList<CraftTask> truePending = new ArrayList<CraftTask>();
        for (CraftTask task = this.head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() == -1) continue;
            truePending.add(task);
        }
        ArrayList<BukkitTask> pending = new ArrayList<BukkitTask>();
        for (CraftTask task : this.runners.values()) {
            if (task.getPeriod() < -1) continue;
            pending.add(task);
        }
        for (CraftTask task : truePending) {
            if (task.getPeriod() < -1 || pending.contains(task)) continue;
            pending.add(task);
        }
        return pending;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void mainThreadHeartbeat(int currentTick) {
        this.currentTick = currentTick;
        List<CraftTask> temp = this.temp;
        this.parsePending();
        while (this.isReady(currentTick)) {
            CraftTask task = this.pending.remove();
            if (task.getPeriod() < -1) {
                if (task.isSync()) {
                    this.runners.remove(task.getTaskId(), task);
                }
                this.parsePending();
                continue;
            }
            if (task.isSync()) {
                this.currentTask = task;
                try {
                    task.timings.startTiming();
                    task.run();
                    task.timings.stopTiming();
                }
                catch (Throwable throwable) {
                    task.getOwner().getLogger().log(Level.WARNING, String.format("Task #%s for %s generated an exception", task.getTaskId(), task.getOwner().getDescription().getFullName()), throwable);
                }
                finally {
                    this.currentTask = null;
                }
                this.parsePending();
            } else {
                this.debugTail = this.debugTail.setNext(new CraftAsyncDebugger(currentTick + RECENT_TICKS, task.getOwner(), task.getTaskClass()));
                this.executor.execute(task);
            }
            long period = task.getPeriod();
            if (period > 0) {
                task.setNextRun((long)currentTick + period);
                temp.add(task);
                continue;
            }
            if (!task.isSync()) continue;
            this.runners.remove(task.getTaskId());
        }
        this.pending.addAll(temp);
        temp.clear();
        this.debugHead = this.debugHead.getNextHead(currentTick);
    }

    private void addTask(CraftTask task) {
        AtomicReference<CraftTask> tail = this.tail;
        CraftTask tailTask = tail.get();
        while (!tail.compareAndSet(tailTask, task)) {
            tailTask = tail.get();
        }
        tailTask.setNext(task);
    }

    private CraftTask handle(CraftTask task, long delay) {
        task.setNextRun((long)this.currentTick + delay);
        this.addTask(task);
        return task;
    }

    private static void validate(Plugin plugin, Object task) {
        Validate.notNull((Object)plugin, (String)"Plugin cannot be null");
        Validate.notNull((Object)task, (String)"Task cannot be null");
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }
    }

    private int nextId() {
        return this.ids.incrementAndGet();
    }

    private void parsePending() {
        CraftTask head = this.head;
        CraftTask task = head.getNext();
        CraftTask lastTask = head;
        while (task != null) {
            if (task.getTaskId() == -1) {
                task.run();
            } else if (task.getPeriod() >= -1) {
                this.pending.add(task);
                this.runners.put(task.getTaskId(), task);
            }
            lastTask = task;
            task = lastTask.getNext();
        }
        task = head;
        while (task != lastTask) {
            head = task.getNext();
            task.setNext(null);
            task = head;
        }
        this.head = lastTask;
    }

    private boolean isReady(int currentTick) {
        return !this.pending.isEmpty() && this.pending.peek().getNextRun() <= (long)currentTick;
    }

    public String toString() {
        int debugTick = this.currentTick;
        StringBuilder string = new StringBuilder("Recent tasks from ").append(debugTick - RECENT_TICKS).append('-').append(debugTick).append('{');
        this.debugHead.debugTo(string);
        return string.append('}').toString();
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay) {
        return this.scheduleSyncDelayedTask(plugin, (Runnable)task, delay);
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task) {
        return this.scheduleSyncDelayedTask(plugin, (Runnable)task);
    }

    @Deprecated
    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period) {
        return this.scheduleSyncRepeatingTask(plugin, (Runnable)task, delay, period);
    }

    @Deprecated
    @Override
    public BukkitTask runTask(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        return this.runTask(plugin, (Runnable)task);
    }

    @Deprecated
    @Override
    public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException {
        return this.runTaskAsynchronously(plugin, (Runnable)task);
    }

    @Deprecated
    @Override
    public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        return this.runTaskLater(plugin, (Runnable)task, delay);
    }

    @Deprecated
    @Override
    public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException {
        return this.runTaskLaterAsynchronously(plugin, (Runnable)task, delay);
    }

    @Deprecated
    @Override
    public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        return this.runTaskTimer(plugin, (Runnable)task, delay, period);
    }

    @Deprecated
    @Override
    public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
        return this.runTaskTimerAsynchronously(plugin, (Runnable)task, delay, period);
    }

}

