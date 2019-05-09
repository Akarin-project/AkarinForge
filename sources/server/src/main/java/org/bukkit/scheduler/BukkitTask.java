/*
 * Akarin Forge
 */
package org.bukkit.scheduler;

import org.bukkit.plugin.Plugin;

public interface BukkitTask {
    public int getTaskId();

    public Plugin getOwner();

    public boolean isSync();

    public boolean isCancelled();

    public void cancel();
}

