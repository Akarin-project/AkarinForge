/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface EnderSignal
extends Entity {
    public Location getTargetLocation();

    public void setTargetLocation(Location var1);

    public boolean getDropItem();

    public void setDropItem(boolean var1);

    public int getDespawnTimer();

    public void setDespawnTimer(int var1);
}

