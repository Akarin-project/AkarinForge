/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftGolem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;

public class CraftSnowman
extends CraftGolem
implements Snowman {
    public CraftSnowman(CraftServer server, aai entity) {
        super(server, entity);
    }

    @Override
    public boolean isDerp() {
        return !this.getHandle().p();
    }

    @Override
    public void setDerp(boolean derpMode) {
        this.getHandle().a(!derpMode);
    }

    @Override
    public aai getHandle() {
        return (aai)this.entity;
    }

    @Override
    public String toString() {
        return "CraftSnowman";
    }

    @Override
    public EntityType getType() {
        return EntityType.SNOWMAN;
    }
}

