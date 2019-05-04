/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
import org.bukkit.entity.Monster;

public class CraftMonster
extends CraftCreature
implements Monster {
    public CraftMonster(CraftServer server, ade entity) {
        super(server, entity);
    }

    @Override
    public ade getHandle() {
        return (ade)this.entity;
    }

    @Override
    public String toString() {
        return "CraftMonster{name=" + this.entityName + "}";
    }
}

