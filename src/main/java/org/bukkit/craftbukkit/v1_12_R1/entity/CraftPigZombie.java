/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftZombie;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie
extends CraftZombie
implements PigZombie {
    public CraftPigZombie(CraftServer server, adf entity) {
        super(server, entity);
    }

    @Override
    public int getAnger() {
        return this.getHandle().bx;
    }

    @Override
    public void setAnger(int level) {
        this.getHandle().bx = level;
    }

    @Override
    public void setAngry(boolean angry) {
        this.setAnger(angry ? 400 : 0);
    }

    @Override
    public boolean isAngry() {
        return this.getAnger() > 0;
    }

    @Override
    public adf getHandle() {
        return (adf)this.entity;
    }

    @Override
    public String toString() {
        return "CraftPigZombie";
    }

    @Override
    public EntityType getType() {
        return EntityType.PIG_ZOMBIE;
    }
}

