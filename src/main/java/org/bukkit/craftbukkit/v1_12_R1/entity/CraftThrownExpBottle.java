/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftProjectile;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle
extends CraftProjectile
implements ThrownExpBottle {
    public CraftThrownExpBottle(CraftServer server, aey entity) {
        super(server, entity);
    }

    @Override
    public aey getHandle() {
        return (aey)this.entity;
    }

    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }

    @Override
    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}

