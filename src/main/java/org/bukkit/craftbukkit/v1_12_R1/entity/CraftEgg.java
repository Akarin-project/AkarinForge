/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftProjectile;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;

public class CraftEgg
extends CraftProjectile
implements Egg {
    public CraftEgg(CraftServer server, aew entity) {
        super(server, entity);
    }

    @Override
    public aew getHandle() {
        return (aew)this.entity;
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }

    @Override
    public EntityType getType() {
        return EntityType.EGG;
    }
}

