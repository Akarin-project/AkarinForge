/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;

public class CraftEvokerFangs
extends CraftEntity
implements EvokerFangs {
    public CraftEvokerFangs(CraftServer server, aej entity) {
        super(server, entity);
    }

    @Override
    public aej getHandle() {
        return (aej)super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEvokerFangs";
    }

    @Override
    public EntityType getType() {
        return EntityType.EVOKER_FANGS;
    }

    @Override
    public LivingEntity getOwner() {
        vp owner = this.getHandle().j();
        return owner == null ? null : (LivingEntity)((Object)owner.getBukkitEntity());
    }

    @Override
    public void setOwner(LivingEntity owner) {
        this.getHandle().a(owner == null ? null : ((CraftLivingEntity)owner).getHandle());
    }
}

