/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;

public class CraftWither
extends CraftMonster
implements Wither {
    public CraftWither(CraftServer server, abx entity) {
        super(server, entity);
    }

    @Override
    public abx getHandle() {
        return (abx)this.entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER;
    }
}

