/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Flying;

public class CraftFlying
extends CraftLivingEntity
implements Flying {
    public CraftFlying(CraftServer server, vn entity) {
        super(server, entity);
    }

    @Override
    public vn getHandle() {
        return (vn)this.entity;
    }

    @Override
    public String toString() {
        return "CraftFlying";
    }
}

