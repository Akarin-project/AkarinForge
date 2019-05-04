/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAgeable;
import org.bukkit.entity.Animals;

public class CraftAnimals
extends CraftAgeable
implements Animals {
    public CraftAnimals(CraftServer server, zv entity) {
        super(server, entity);
    }

    @Override
    public zv getHandle() {
        return (zv)this.entity;
    }

    @Override
    public String toString() {
        return "CraftAnimals{name=" + this.entityName + "}";
    }
}

