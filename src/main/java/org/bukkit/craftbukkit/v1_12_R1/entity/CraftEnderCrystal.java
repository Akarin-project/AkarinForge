/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

public class CraftEnderCrystal
extends CraftEntity
implements EnderCrystal {
    public CraftEnderCrystal(CraftServer server, abc entity) {
        super(server, entity);
    }

    @Override
    public boolean isShowingBottom() {
        return this.getHandle().k();
    }

    @Override
    public void setShowingBottom(boolean showing) {
        this.getHandle().a(showing);
    }

    @Override
    public Location getBeamTarget() {
        et pos = this.getHandle().j();
        return pos == null ? null : new Location(this.getWorld(), pos.p(), pos.q(), pos.r());
    }

    @Override
    public void setBeamTarget(Location location) {
        if (location == null) {
            this.getHandle().a((et)null);
        } else {
            if (location.getWorld() != this.getWorld()) {
                throw new IllegalArgumentException("Cannot set beam target location to different world");
            }
            this.getHandle().a(new et(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }
    }

    @Override
    public abc getHandle() {
        return (abc)this.entity;
    }

    @Override
    public String toString() {
        return "CraftEnderCrystal";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_CRYSTAL;
    }
}

