/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed
extends CraftEntity
implements TNTPrimed {
    public CraftTNTPrimed(CraftServer server, acm entity) {
        super(server, entity);
    }

    @Override
    public float getYield() {
        return this.getHandle().yield;
    }

    @Override
    public boolean isIncendiary() {
        return this.getHandle().isIncendiary;
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        this.getHandle().isIncendiary = isIncendiary;
    }

    @Override
    public void setYield(float yield) {
        this.getHandle().yield = yield;
    }

    @Override
    public int getFuseTicks() {
        return this.getHandle().l();
    }

    @Override
    public void setFuseTicks(int fuseTicks) {
        this.getHandle().a(fuseTicks);
    }

    @Override
    public acm getHandle() {
        return (acm)this.entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    @Override
    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

    @Override
    public Entity getSource() {
        vp source = this.getHandle().j();
        return source != null ? source.getBukkitEntity() : null;
    }
}

