/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;

public class CraftSilverfish
extends CraftMonster
implements Silverfish {
    public CraftSilverfish(CraftServer server, adj entity) {
        super(server, entity);
    }

    @Override
    public adj getHandle() {
        return (adj)this.entity;
    }

    @Override
    public String toString() {
        return "CraftSilverfish";
    }

    @Override
    public EntityType getType() {
        return EntityType.SILVERFISH;
    }
}

