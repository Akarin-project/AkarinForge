/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFireball;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull
extends CraftFireball
implements WitherSkull {
    public CraftWitherSkull(CraftServer server, afb entity) {
        super(server, entity);
    }

    @Override
    public void setCharged(boolean charged) {
        this.getHandle().a(charged);
    }

    @Override
    public boolean isCharged() {
        return this.getHandle().n();
    }

    @Override
    public afb getHandle() {
        return (afb)this.entity;
    }

    @Override
    public String toString() {
        return "CraftWitherSkull";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER_SKULL;
    }
}

