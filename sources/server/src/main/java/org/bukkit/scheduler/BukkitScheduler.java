/*
 * Akarin Forge
 */
package org.bukkit.scheduler;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

public interface BukkitScheduler {
    public int scheduleSyncDelayedTask(Plugin var1, Runnable var2, long var3);

    @Deprecated
    public int scheduleSyncDelayedTask(Plugin var1, BukkitRunnable var2, long var3);

    public int scheduleSyncDelayedTask(Plugin var1, Runnable var2);

    @Deprecated
    public int scheduleSyncDelayedTask(Plugin var1, BukkitRunnable var2);

    public int scheduleSyncRepeatingTask(Plugin var1, Runnable var2, long var3, long var5);

    @Deprecated
    public int scheduleSyncRepeatingTask(Plugin var1, BukkitRunnable var2, long var3, long var5);

    @Deprecated
    public int scheduleAsyncDelayedTask(Plugin var1, Runnable var2, long var3);

    @Deprecated
    public int scheduleAsyncDelayedTask(Plugin var1, Runnable var2);

    @Deprecated
    public int scheduleAsyncRepeatingTask(Plugin var1, Runnable var2, long var3, long var5);

    public <T> Future<T> callSyncMethod(Plugin var1, Callable<T> var2);

    public void cancelTask(int var1);

    public void cancelTasks(Plugin var1);

    public void cancelAllTasks();

    public boolean isCurrentlyRunning(int var1);

    public boolean isQueued(int var1);

    public List<BukkitWorker> getActiveWorkers();

    public List<BukkitTask> getPendingTasks();

    public BukkitTask runTask(Plugin var1, Runnable var2) throws IllegalArgumentException;

    @Deprecated
    public BukkitTask runTask(Plugin var1, BukkitRunnable var2) throws IllegalArgumentException;

    public BukkitTask runTaskAsynchronously(Plugin var1, Runnable var2) throws IllegalArgumentException;

    @Deprecated
    public BukkitTask runTaskAsynchronously(Plugin var1, BukkitRunnable var2) throws IllegalArgumentException;

    public BukkitTask runTaskLater(Plugin var1, Runnable var2, long var3) throws IllegalArgumentException;

    @Deprecated
    public BukkitTask runTaskLater(Plugin var1, BukkitRunnable var2, long var3) throws IllegalArgumentException;

    public BukkitTask runTaskLaterAsynchronously(Plugin var1, Runnable var2, long var3) throws IllegalArgumentException;

    @Deprecated
    public BukkitTask runTaskLaterAsynchronously(Plugin var1, BukkitRunnable var2, long var3) throws IllegalArgumentException;

    public BukkitTask runTaskTimer(Plugin var1, Runnable var2, long var3, long var5) throws IllegalArgumentException;

    @Deprecated
    public BukkitTask runTaskTimer(Plugin var1, BukkitRunnable var2, long var3, long var5) throws IllegalArgumentException;

    public BukkitTask runTaskTimerAsynchronously(Plugin var1, Runnable var2, long var3, long var5) throws IllegalArgumentException;

    @Deprecated
    public BukkitTask runTaskTimerAsynchronously(Plugin var1, BukkitRunnable var2, long var3, long var5) throws IllegalArgumentException;
}

