/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Weather;

public class CraftWeather
extends CraftEntity
implements Weather {
    public CraftWeather(CraftServer server, ach entity) {
        super(server, entity);
    }

    @Override
    public ach getHandle() {
        return (ach)this.entity;
    }

    @Override
    public String toString() {
        return "CraftWeather";
    }

    @Override
    public EntityType getType() {
        return EntityType.WEATHER;
    }
}

