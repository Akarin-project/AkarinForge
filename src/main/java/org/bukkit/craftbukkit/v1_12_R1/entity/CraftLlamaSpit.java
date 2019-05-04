/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.AbstractProjectile;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.projectiles.ProjectileSource;

public class CraftLlamaSpit
extends AbstractProjectile
implements LlamaSpit {
    public CraftLlamaSpit(CraftServer server, aeo entity) {
        super(server, entity);
    }

    @Override
    public aeo getHandle() {
        return (aeo)super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftLlamaSpit";
    }

    @Override
    public EntityType getType() {
        return EntityType.LLAMA_SPIT;
    }

    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().a != null ? (ProjectileSource)((Object)this.getHandle().a.getBukkitEntity()) : null;
    }

    @Override
    public void setShooter(ProjectileSource source) {
        this.getHandle().a = source != null ? ((CraftLivingEntity)source).getHandle() : null;
    }
}

