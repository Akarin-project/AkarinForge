/*
 * Akarin Forge
 */
package org.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class BukkitRunnable
implements Runnable {
    private BukkitTask task;

    public synchronized boolean isCancelled() throws IllegalStateException {
        this.checkScheduled();
        return this.task.isCancelled();
    }

    public synchronized void cancel() throws IllegalStateException {
        Bukkit.getScheduler().cancelTask(this.getTaskId());
    }

    public synchronized BukkitTask runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(Bukkit.getScheduler().runTask(plugin, (Runnable)this));
    }

    public synchronized BukkitTask runTaskAsynchronously(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, (Runnable)this));
    }

    public synchronized BukkitTask runTaskLater(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(Bukkit.getScheduler().runTaskLater(plugin, (Runnable)this, delay));
    }

    public synchronized BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, (Runnable)this, delay));
    }

    public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(Bukkit.getScheduler().runTaskTimer(plugin, (Runnable)this, delay, period));
    }

    public synchronized BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, (Runnable)this, delay, period));
    }

    public synchronized int getTaskId() throws IllegalStateException {
        this.checkScheduled();
        return this.task.getTaskId();
    }

    private void checkScheduled() {
        if (this.task == null) {
            throw new IllegalStateException("Not scheduled yet");
        }
    }

    private void checkNotYetScheduled() {
        if (this.task != null) {
            throw new IllegalStateException("Already scheduled as " + this.task.getTaskId());
        }
    }

    private BukkitTask setupTask(BukkitTask task) {
        this.task = task;
        return task;
    }
}

