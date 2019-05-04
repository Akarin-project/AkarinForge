/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;

public class CraftEnderSignal
extends CraftEntity
implements EnderSignal {
    public CraftEnderSignal(CraftServer server, aek entity) {
        super(server, entity);
    }

    @Override
    public aek getHandle() {
        return (aek)this.entity;
    }

    @Override
    public String toString() {
        return "CraftEnderSignal";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_SIGNAL;
    }

    @Override
    public Location getTargetLocation() {
        return new Location(this.getWorld(), this.getHandle().a, this.getHandle().b, this.getHandle().c, this.getHandle().v, this.getHandle().w);
    }

    @Override
    public void setTargetLocation(Location location) {
        Preconditions.checkArgument((boolean)this.getWorld().equals(location.getWorld()), (Object)"Cannot target EnderSignal across worlds");
        this.getHandle().a(new et(location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public boolean getDropItem() {
        return this.getHandle().e;
    }

    @Override
    public void setDropItem(boolean shouldDropItem) {
        this.getHandle().e = shouldDropItem;
    }

    @Override
    public int getDespawnTimer() {
        return this.getHandle().d;
    }

    @Override
    public void setDespawnTimer(int time) {
        this.getHandle().d = time;
    }
}

