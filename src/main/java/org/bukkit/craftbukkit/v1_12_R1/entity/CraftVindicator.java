/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftIllager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vindicator;

public class CraftVindicator
extends CraftIllager
implements Vindicator {
    public CraftVindicator(CraftServer server, adq entity) {
        super(server, entity);
    }

    @Override
    public adq getHandle() {
        return (adq)super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftVindicator";
    }

    @Override
    public EntityType getType() {
        return EntityType.VINDICATOR;
    }
}

