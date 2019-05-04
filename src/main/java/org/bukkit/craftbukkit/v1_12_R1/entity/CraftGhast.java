/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFlying;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;

public class CraftGhast
extends CraftFlying
implements Ghast {
    public CraftGhast(CraftServer server, acy entity) {
        super(server, entity);
    }

    @Override
    public acy getHandle() {
        return (acy)this.entity;
    }

    @Override
    public String toString() {
        return "CraftGhast";
    }

    @Override
    public EntityType getType() {
        return EntityType.GHAST;
    }
}

