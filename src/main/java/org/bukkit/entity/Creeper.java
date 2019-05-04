/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.entity.Monster;

public interface Creeper
extends Monster {
    public boolean isPowered();

    public void setPowered(boolean var1);

    public void setMaxFuseTicks(int var1);

    public int getMaxFuseTicks();

    public void setExplosionRadius(int var1);

    public int getExplosionRadius();
}

