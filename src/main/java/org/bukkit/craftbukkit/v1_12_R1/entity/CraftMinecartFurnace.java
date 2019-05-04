/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.PoweredMinecart;

public class CraftMinecartFurnace
extends CraftMinecart
implements PoweredMinecart {
    public CraftMinecartFurnace(CraftServer server, afi entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartFurnace";
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART_FURNACE;
    }
}

