/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftSpider;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;

public class CraftCaveSpider
extends CraftSpider
implements CaveSpider {
    public CraftCaveSpider(CraftServer server, acr entity) {
        super(server, entity);
    }

    @Override
    public acr getHandle() {
        return (acr)this.entity;
    }

    @Override
    public String toString() {
        return "CraftCaveSpider";
    }

    @Override
    public EntityType getType() {
        return EntityType.CAVE_SPIDER;
    }
}

