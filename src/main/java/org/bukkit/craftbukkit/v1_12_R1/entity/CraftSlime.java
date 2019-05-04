/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityTargetEvent;

public class CraftSlime
extends CraftLivingEntity
implements Slime {
    public CraftSlime(CraftServer server, adl entity) {
        super(server, entity);
    }

    @Override
    public int getSize() {
        return this.getHandle().dl();
    }

    @Override
    public void setSize(int size) {
        this.getHandle().a(size, true);
    }

    @Override
    public void setTarget(LivingEntity target) {
        if (target == null) {
            this.getHandle().setGoalTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            this.getHandle().setGoalTarget(((CraftLivingEntity)target).getHandle(), null, false);
        }
    }

    @Override
    public LivingEntity getTarget() {
        return this.getHandle().z() == null ? null : (LivingEntity)((Object)this.getHandle().z().getBukkitEntity());
    }

    @Override
    public adl getHandle() {
        return (adl)this.entity;
    }

    @Override
    public String toString() {
        return "CraftSlime";
    }

    @Override
    public EntityType getType() {
        return EntityType.SLIME;
    }
}

