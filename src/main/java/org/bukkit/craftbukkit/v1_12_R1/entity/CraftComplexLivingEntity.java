/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity
extends CraftLivingEntity
implements ComplexLivingEntity {
    public CraftComplexLivingEntity(CraftServer server, vq entity) {
        super(server, entity);
    }

    @Override
    public vq getHandle() {
        return (vq)this.entity;
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}

