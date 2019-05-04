/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.EntityType;

public class CraftAmbient
extends CraftLivingEntity
implements Ambient {
    public CraftAmbient(CraftServer server, zs entity) {
        super(server, entity);
    }

    @Override
    public zs getHandle() {
        return (zs)this.entity;
    }

    @Override
    public String toString() {
        return "CraftAmbient{name=" + this.entityName + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}

