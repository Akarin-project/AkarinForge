/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.AbstractProjectile;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CraftFireball
extends AbstractProjectile
implements Fireball {
    public CraftFireball(CraftServer server, ael entity) {
        super(server, entity);
    }

    @Override
    public float getYield() {
        return this.getHandle().bukkitYield;
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
        this.getHandle().bukkitYield = yield;
    }

    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        this.getHandle().a = shooter instanceof CraftLivingEntity ? ((CraftLivingEntity)shooter).getHandle() : null;
        this.getHandle().projectileSource = shooter;
    }

    @Override
    public Vector getDirection() {
        return new Vector(this.getHandle().b, this.getHandle().c, this.getHandle().d);
    }

    @Override
    public void setDirection(Vector direction) {
        Validate.notNull((Object)direction, (String)"Direction can not be null", (Object[])new Object[0]);
        double x2 = direction.getX();
        double y2 = direction.getY();
        double z2 = direction.getZ();
        double magnitude = rk.a(x2 * x2 + y2 * y2 + z2 * z2);
        this.getHandle().b = x2 / magnitude;
        this.getHandle().c = y2 / magnitude;
        this.getHandle().d = z2 / magnitude;
    }

    @Override
    public ael getHandle() {
        return (ael)this.entity;
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}

