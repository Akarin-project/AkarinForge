/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.AbstractProjectile;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class CraftProjectile
extends AbstractProjectile
implements Projectile {
    public CraftProjectile(CraftServer server, vg entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().c = (vq)((CraftLivingEntity)shooter).entity;
            if (shooter instanceof CraftHumanEntity) {
                this.getHandle().at = ((CraftHumanEntity)shooter).getName();
            }
        } else {
            this.getHandle().c = null;
            this.getHandle().at = null;
        }
        this.getHandle().projectileSource = shooter;
    }

    @Override
    public aev getHandle() {
        return (aev)this.entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}

