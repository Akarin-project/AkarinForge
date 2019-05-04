/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;

public class CraftLargeFireball
extends CraftFireball
implements LargeFireball {
    public CraftLargeFireball(CraftServer server, aen entity) {
        super(server, entity);
    }

    @Override
    public void setYield(float yield) {
        super.setYield(yield);
        this.getHandle().e = (int)yield;
    }

    @Override
    public aen getHandle() {
        return (aen)this.entity;
    }

    @Override
    public String toString() {
        return "CraftLargeFireball";
    }

    @Override
    public EntityType getType() {
        return EntityType.FIREBALL;
    }
}

