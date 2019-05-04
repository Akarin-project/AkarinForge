/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.WaterMob;

public class CraftWaterMob
extends CraftLivingEntity
implements WaterMob {
    public CraftWaterMob(CraftServer server, aal entity) {
        super(server, entity);
    }

    @Override
    public aal getHandle() {
        return (aal)this.entity;
    }

    @Override
    public String toString() {
        return "CraftWaterMob{name=" + this.entityName + "}";
    }
}

