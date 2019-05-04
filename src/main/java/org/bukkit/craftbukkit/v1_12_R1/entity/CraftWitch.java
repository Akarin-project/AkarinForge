/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;

public class CraftWitch
extends CraftMonster
implements Witch {
    public CraftWitch(CraftServer server, adr entity) {
        super(server, entity);
    }

    @Override
    public adr getHandle() {
        return (adr)this.entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITCH;
    }
}

