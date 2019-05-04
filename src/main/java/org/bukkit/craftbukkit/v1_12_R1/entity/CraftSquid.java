/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWaterMob;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;

public class CraftSquid
extends CraftWaterMob
implements Squid {
    public CraftSquid(CraftServer server, aaj entity) {
        super(server, entity);
    }

    @Override
    public aaj getHandle() {
        return (aaj)this.entity;
    }

    @Override
    public String toString() {
        return "CraftSquid";
    }

    @Override
    public EntityType getType() {
        return EntityType.SQUID;
    }
}

