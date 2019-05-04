/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.AbstractProjectile;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.projectiles.ProjectileSource;

public class CraftShulkerBullet
extends AbstractProjectile
implements ShulkerBullet {
    public CraftShulkerBullet(CraftServer server, aer entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            this.getHandle().setShooter(((CraftLivingEntity)shooter).getHandle());
        } else {
            this.getHandle().setShooter(null);
        }
        this.getHandle().projectileSource = shooter;
    }

    @Override
    public Entity getTarget() {
        return this.getHandle().getTarget() != null ? this.getHandle().getTarget().getBukkitEntity() : null;
    }

    @Override
    public void setTarget(Entity target) {
        this.getHandle().setTarget(target == null ? null : ((CraftEntity)target).getHandle());
    }

    @Override
    public String toString() {
        return "CraftShulkerBullet";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHULKER_BULLET;
    }

    @Override
    public aer getHandle() {
        return (aer)this.entity;
    }
}

