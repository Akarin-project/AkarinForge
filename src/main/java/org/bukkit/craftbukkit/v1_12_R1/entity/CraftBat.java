/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAmbient;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;

public class CraftBat
extends CraftAmbient
implements Bat {
    public CraftBat(CraftServer server, zt entity) {
        super(server, entity);
    }

    @Override
    public zt getHandle() {
        return (zt)this.entity;
    }

    @Override
    public String toString() {
        return "CraftBat";
    }

    @Override
    public EntityType getType() {
        return EntityType.BAT;
    }

    @Override
    public boolean isAwake() {
        return !this.getHandle().p();
    }

    @Override
    public void setAwake(boolean state) {
        this.getHandle().a(!state);
    }
}

