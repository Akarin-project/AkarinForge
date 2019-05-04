/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;

public class CraftCreature
extends CraftLivingEntity
implements Creature {
    public CraftCreature(CraftServer server, vx entity) {
        super(server, entity);
    }

    @Override
    public void setTarget(LivingEntity target) {
        vx entity = this.getHandle();
        if (target == null) {
            entity.setGoalTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            entity.setGoalTarget(((CraftLivingEntity)target).getHandle(), null, false);
        }
    }

    @Override
    public CraftLivingEntity getTarget() {
        if (this.getHandle().z() == null) {
            return null;
        }
        return (CraftLivingEntity)this.getHandle().z().getBukkitEntity();
    }

    @Override
    public vx getHandle() {
        return (vx)this.entity;
    }

    @Override
    public String toString() {
        return "CraftCreature{name=" + this.entityName + "}";
    }
}

