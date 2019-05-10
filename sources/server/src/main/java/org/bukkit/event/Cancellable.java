/*
 * Akarin Forge
 */
package org.bukkit.event;

public interface Cancellable {
    public boolean isCancelled();

    public void setCancelled(boolean var1);
}

