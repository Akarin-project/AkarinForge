/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAnimals;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;

public class CraftChicken
extends CraftAnimals
implements Chicken {
    public CraftChicken(CraftServer server, zw entity) {
        super(server, entity);
    }

    @Override
    public zw getHandle() {
        return (zw)this.entity;
    }

    @Override
    public String toString() {
        return "CraftChicken";
    }

    @Override
    public EntityType getType() {
        return EntityType.CHICKEN;
    }
}

