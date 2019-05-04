/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface EnderCrystal
extends Entity {
    public boolean isShowingBottom();

    public void setShowingBottom(boolean var1);

    public Location getBeamTarget();

    public void setBeamTarget(Location var1);
}

