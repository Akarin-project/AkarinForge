/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftGolem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

public class CraftIronGolem
extends CraftGolem
implements IronGolem {
    public CraftIronGolem(CraftServer server, aak entity) {
        super(server, entity);
    }

    @Override
    public aak getHandle() {
        return (aak)this.entity;
    }

    @Override
    public String toString() {
        return "CraftIronGolem";
    }

    @Override
    public boolean isPlayerCreated() {
        return this.getHandle().dn();
    }

    @Override
    public void setPlayerCreated(boolean playerCreated) {
        this.getHandle().p(playerCreated);
    }

    @Override
    public EntityType getType() {
        return EntityType.IRON_GOLEM;
    }
}

