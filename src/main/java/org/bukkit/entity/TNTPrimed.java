/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;

public interface TNTPrimed
extends Explosive {
    public void setFuseTicks(int var1);

    public int getFuseTicks();

    public Entity getSource();
}

