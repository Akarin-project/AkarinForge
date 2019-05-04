/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;

public class CraftSpider
extends CraftMonster
implements Spider {
    public CraftSpider(CraftServer server, adn entity) {
        super(server, entity);
    }

    @Override
    public adn getHandle() {
        return (adn)this.entity;
    }

    @Override
    public String toString() {
        return "CraftSpider";
    }

    @Override
    public EntityType getType() {
        return EntityType.SPIDER;
    }
}

