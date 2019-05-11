package org.bukkit.craftbukkit.v1_12_R1.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CraftTask
implements BukkitTask,
Runnable {
    private volatile CraftTask next = null;
    public static final int ERROR = 0;
    public static final int NO_REPEATING = -1;
    public static final int CANCEL = -2;
    public static final int PROCESS_FOR_FUTURE = -3;
    public static final int DONE_FOR_FUTURE = -4;
    private volatile long period;
    private long nextRun;
    private final Runnable task;
    private final Plugin plugin;
    private final int id;
    public String timingName = null;

    CraftTask() {
        this(null, null, -1, -1);
    }

    CraftTask(Runnable task) {
        this(null, task, -1, -1);
    }

    CraftTask(String timingName) {
        this(timingName, null, null, -1, -1);
    }

    CraftTask(String timingName, Runnable task) {
        this(timingName, null, task, -1, -1);
    }

    CraftTask(String timingName, Plugin plugin, Runnable task, int id2, long period) {
        this.plugin = plugin;
        this.task = task;
        this.id = id2;
        this.period = period;
        this.timingName = timingName == null && task == null ? "Unknown" : timingName;
    }

    CraftTask(Plugin plugin, Runnable task, int id2, long period) {
        this(null, plugin, task, id2, period);
    }

    @Override
    public final int getTaskId() {
        return this.id;
    }

    @Override
    public final Plugin getOwner() {
        return this.plugin;
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public void run() {
        this.task.run();
    }

    long getPeriod() {
        return this.period;
    }

    void setPeriod(long period) {
        this.period = period;
    }

    long getNextRun() {
        return this.nextRun;
    }

    void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    CraftTask getNext() {
        return this.next;
    }

    void setNext(CraftTask next) {
        this.next = next;
    }

    public Class<? extends Runnable> getTaskClass() { // Akarin
        return this.task.getClass();
    }

    @Override
    public boolean isCancelled() {
        return this.period == -2;
    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.id);
    }

    boolean cancel0() {
        this.setPeriod(-2);
        return true;
    }

    public String getTaskName() {
        if (this.timingName != null) {
            return this.timingName;
        }
        return this.task.getClass().getName();
    }
}

