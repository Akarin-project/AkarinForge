/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.Validate;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.AbstractProjectile;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;

public class CraftArrow
extends AbstractProjectile
implements Arrow {
    private final Arrow.Spigot spigot;

    public CraftArrow(CraftServer server, aeh entity) {
        super(server, entity);
        this.spigot = new Arrow.Spigot(){

            @Override
            public double getDamage() {
                return CraftArrow.this.getHandle().k();
            }

            @Override
            public void setDamage(double damage) {
                CraftArrow.this.getHandle().c(damage);
            }
        };
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
        Validate.isTrue((boolean)(knockbackStrength >= 0), (String)"Knockback cannot be negative", (Object[])new Object[0]);
        this.getHandle().a(knockbackStrength);
    }

    @Override
    public int getKnockbackStrength() {
        return this.getHandle().aA;
    }

    @Override
    public boolean isCritical() {
        return this.getHandle().n();
    }

    @Override
    public void setCritical(boolean critical) {
        this.getHandle().a(critical);
    }

    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        this.getHandle().e = shooter instanceof LivingEntity ? ((CraftLivingEntity)shooter).getHandle() : null;
        this.getHandle().projectileSource = shooter;
    }

    @Override
    public boolean isInBlock() {
        return this.getHandle().z;
    }

    @Override
    public Block getAttachedBlock() {
        if (!this.isInBlock()) {
            return null;
        }
        aeh handle = this.getHandle();
        return this.getWorld().getBlockAt(handle.h, handle.at, handle.au);
    }

    @Override
    public Arrow.PickupStatus getPickupStatus() {
        return Arrow.PickupStatus.values()[this.getHandle().c.ordinal()];
    }

    @Override
    public void setPickupStatus(Arrow.PickupStatus status) {
        Preconditions.checkNotNull((Object)((Object)status), (Object)"status");
        this.getHandle().c = aeh.a.a(status.ordinal());
    }

    @Override
    public aeh getHandle() {
        return (aeh)this.entity;
    }

    @Override
    public String toString() {
        return "CraftArrow";
    }

    @Override
    public EntityType getType() {
        return EntityType.ARROW;
    }

    @Override
    public Arrow.Spigot org_bukkit_entity_Arrow$Spigot_spigot() {
        return this.spigot;
    }

}

