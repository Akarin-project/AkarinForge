/*
 * Akarin Forge
 */
package org.bukkit.scheduler;

import org.bukkit.plugin.Plugin;

public interface BukkitWorker {
    public int getTaskId();

    public Plugin getOwner();

    public Thread getThread();
}

