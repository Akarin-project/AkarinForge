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
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.projectiles.ProjectileSource;

public class CraftFish
extends AbstractProjectile
implements Fish {
    private double biteChance = -1.0;

    public CraftFish(CraftServer server, acf entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        if (this.getHandle().e != null) {
            return this.getHandle().e.getBukkitEntity();
        }
        return null;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftHumanEntity) {
            this.getHandle().e = (aed)((CraftHumanEntity)shooter).entity;
        }
    }

    @Override
    public acf getHandle() {
        return (acf)this.entity;
    }

    @Override
    public String toString() {
        return "CraftFish";
    }

    @Override
    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }

    @Override
    public double getBiteChance() {
        acf hook = this.getHandle();
        if (this.biteChance == -1.0) {
            if (hook.l.B(new et(rk.c(hook.p), rk.c(hook.q) + 1, rk.c(hook.r)))) {
                return 0.0033333333333333335;
            }
            return 0.002;
        }
        return this.biteChance;
    }

    @Override
    public void setBiteChance(double chance) {
        Validate.isTrue((boolean)(chance >= 0.0 && chance <= 1.0), (String)"The bite chance must be between 0 and 1.", (Object[])new Object[0]);
        this.biteChance = chance;
    }
}

