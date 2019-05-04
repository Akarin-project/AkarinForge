/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAnimals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

public class CraftPig
extends CraftAnimals
implements Pig {
    public CraftPig(CraftServer server, aad entity) {
        super(server, entity);
    }

    @Override
    public boolean hasSaddle() {
        return this.getHandle().dl();
    }

    @Override
    public void setSaddle(boolean saddled) {
        this.getHandle().p(saddled);
    }

    @Override
    public aad getHandle() {
        return (aad)this.entity;
    }

    @Override
    public String toString() {
        return "CraftPig";
    }

    @Override
    public EntityType getType() {
        return EntityType.PIG;
    }
}

