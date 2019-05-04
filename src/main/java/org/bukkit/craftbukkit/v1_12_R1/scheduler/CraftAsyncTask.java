/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.craftbukkit.v1_12_R1.scheduler.CraftTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitWorker;

class CraftAsyncTask
extends CraftTask {
    private final LinkedList<BukkitWorker> workers = new LinkedList();
    private final Map<Integer, CraftTask> runners;

    CraftAsyncTask(Map<Integer, CraftTask> runners, Plugin plugin, Runnable task, int id2, long delay) {
        super(plugin, task, id2, delay);
        this.runners = runners;
    }

    @Override
    public boolean isSync() {
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        LinkedList<BukkitWorker> linkedList;
        final Thread thread = Thread.currentThread();
        LinkedList<BukkitWorker> linkedList2 = this.workers;
        synchronized (linkedList2) {
            if (this.getPeriod() == -2) {
                return;
            }
            this.workers.add(()new BukkitWorker(){

                @Override
                public Thread getThread() {
                    return thread;
                }

                @Override
                public int getTaskId() {
                    return CraftAsyncTask.this.getTaskId();
                }

                @Override
                public Plugin getOwner() {
                    return CraftAsyncTask.this.getOwner();
                }
            });
        }
        Throwable thrown = null;
        try {
            super.run();
            linkedList = this.workers;
        }
        catch (Throwable t22) {
            LinkedList<BukkitWorker> t22;
            try {
                thrown = t22;
                this.getOwner().getLogger().log(Level.WARNING, String.format("Plugin %s generated an exception while executing task %s", this.getOwner().getDescription().getFullName(), this.getTaskId()), thrown);
                t22 = this.workers;
            }
            catch (Throwable throwable) {
                LinkedList<BukkitWorker> linkedList3 = this.workers;
                synchronized (linkedList3) {
                    try {
                        Iterator<BukkitWorker> workers = this.workers.iterator();
                        boolean removed = false;
                        while (workers.hasNext()) {
                            if (workers.next().getThread() != thread) continue;
                            workers.remove();
                            removed = true;
                            break;
                        }
                        if (!removed) {
                            throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getDescription().getFullName()), thrown);
                        }
                    }
                    finally {
                        if (this.getPeriod() < 0 && this.workers.isEmpty()) {
                            this.runners.remove(this.getTaskId());
                        }
                    }
                }
                throw throwable;
            }
            synchronized (t22) {
                try {
                    Iterator<BukkitWorker> workers = this.workers.iterator();
                    boolean removed = false;
                    while (workers.hasNext()) {
                        if (workers.next().getThread() != thread) continue;
                        workers.remove();
                        removed = true;
                        break;
                    }
                    if (!removed) {
                        throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getDescription().getFullName()), thrown);
                    }
                }
                finally {
                    if (this.getPeriod() < 0 && this.workers.isEmpty()) {
                        this.runners.remove(this.getTaskId());
                    }
                }
            }
        }
        synchronized (linkedList) {
            try {
                Iterator<BukkitWorker> workers = this.workers.iterator();
                boolean removed = false;
                while (workers.hasNext()) {
                    if (workers.next().getThread() != thread) continue;
                    workers.remove();
                    removed = true;
                    break;
                }
                if (!removed) {
                    throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getDescription().getFullName()), thrown);
                }
            }
            finally {
                if (this.getPeriod() < 0 && this.workers.isEmpty()) {
                    this.runners.remove(this.getTaskId());
                }
            }
        }
    }

    LinkedList<BukkitWorker> getWorkers() {
        return this.workers;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    boolean cancel0() {
        LinkedList<BukkitWorker> linkedList = this.workers;
        synchronized (linkedList) {
            this.setPeriod(-2);
            if (this.workers.isEmpty()) {
                this.runners.remove(this.getTaskId());
            }
        }
        return true;
    }

}

