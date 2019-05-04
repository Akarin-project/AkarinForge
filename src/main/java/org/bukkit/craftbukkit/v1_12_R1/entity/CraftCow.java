/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAnimals;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;

public class CraftCow
extends CraftAnimals
implements Cow {
    public CraftCow(CraftServer server, zx entity) {
        super(server, entity);
    }

    @Override
    public zx getHandle() {
        return (zx)this.entity;
    }

    @Override
    public String toString() {
        return "CraftCow";
    }

    @Override
    public EntityType getType() {
        return EntityType.COW;
    }
}

