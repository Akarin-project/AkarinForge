/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.plugin.PluginManager;

public class CraftCreeper
extends CraftMonster
implements Creeper {
    public CraftCreeper(CraftServer server, acs entity) {
        super(server, entity);
    }

    @Override
    public boolean isPowered() {
        return this.getHandle().p();
    }

    @Override
    public void setPowered(boolean powered) {
        CraftServer server = this.server;
        Creeper entity = (Creeper)((Object)this.getHandle().getBukkitEntity());
        if (powered) {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_ON);
            server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.getHandle().setPowered(true);
            }
        } else {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_OFF);
            server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.getHandle().setPowered(false);
            }
        }
    }

    @Override
    public void setMaxFuseTicks(int ticks) {
        Preconditions.checkArgument((boolean)(ticks >= 0), (Object)"ticks < 0");
        this.getHandle().bz = ticks;
    }

    @Override
    public int getMaxFuseTicks() {
        return this.getHandle().bz;
    }

    @Override
    public void setExplosionRadius(int radius) {
        Preconditions.checkArgument((boolean)(radius >= 0), (Object)"radius < 0");
        this.getHandle().bA = radius;
    }

    @Override
    public int getExplosionRadius() {
        return this.getHandle().bA;
    }

    @Override
    public acs getHandle() {
        return (acs)this.entity;
    }

    @Override
    public String toString() {
        return "CraftCreeper";
    }

    @Override
    public EntityType getType() {
        return EntityType.CREEPER;
    }
}

