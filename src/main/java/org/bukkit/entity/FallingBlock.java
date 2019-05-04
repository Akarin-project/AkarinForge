/*
 * Akarin Forge
 */
package org.bukkit.entity;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

public interface FallingBlock
extends Entity {
    public Material getMaterial();

    @Deprecated
    public int getBlockId();

    @Deprecated
    public byte getBlockData();

    public boolean getDropItem();

    public void setDropItem(boolean var1);

    public boolean canHurtEntities();

    public void setHurtEntities(boolean var1);
}

